package com.bbva.rbvd.lib.r403.business.impl;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.elara.utility.api.connector.APIConnector;
import com.bbva.pisd.lib.r014.PISDR014;
import com.bbva.pisd.lib.r402.PISDR402;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.*;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.rimac.FinancingBO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.rimac.ParticularDataBO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.rimac.PlanBO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.dao.InsuranceModalityDAO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.InsuranceEnterpriseInputBO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.InsuranceEnterpriseResponseBO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.QuotationInputBO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.QuotationResponseBO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.QuotationBO;
import com.bbva.rbvd.dto.enterpriseinsurance.utils.ConstantsUtil;
import com.bbva.rbvd.lib.r403.business.IInsrEnterpriseLifeBusiness;
import com.bbva.rbvd.lib.r403.service.dao.IEnterprisePlanSelectedDAO;
import com.bbva.rbvd.lib.r403.service.dao.IEnterprisePolicyQuotaInternalIdDAO;
import com.bbva.rbvd.lib.r403.service.dao.impl.EnterprisePlanSelectedDAO;
import com.bbva.rbvd.lib.r403.service.dao.impl.EnterprisePolicyQuotaInternalIdDAO;
import com.bbva.rbvd.lib.r403.service.api.ConsumerExternalService;
import com.bbva.rbvd.lib.r403.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r403.transfer.PayloadStore;
import com.bbva.rbvd.lib.r403.utils.ContansUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;


public class InsrEnterpriseLifeBusinessImpl implements IInsrEnterpriseLifeBusiness {
    private static final Logger LOGGER = LoggerFactory.getLogger(InsrEnterpriseLifeBusinessImpl.class);
    private final ApplicationConfigurationService applicationConfigurationService;
    private final APIConnector externalApiConnector;
    private final PISDR014 pisdR014;
    private final PISDR402 pisdR402;
    public InsrEnterpriseLifeBusinessImpl(ApplicationConfigurationService applicationConfigurationService,
                                          APIConnector externalApiConnector,PISDR014 pisdR014, PISDR402 pisdR402) {
        this.applicationConfigurationService = applicationConfigurationService;
        this.externalApiConnector = externalApiConnector;
        this.pisdR014 = pisdR014;
        this.pisdR402 = pisdR402;
    }
    @Override
    public PayloadStore doEnterpriseLife(PayloadConfig payloadConfig) {
        LOGGER.info("***** InsrEnterpriseLifeBusinessImpl - doEnterpriseLife START | argument payloadConfig: {} *****",
                payloadConfig);

        PayloadStore payloadStore = new PayloadStore();
        if(payloadConfig.getInput().getQuotationReference()!=null) {
            List<String> policyQuotaInternalId = getPolicyIdInfo(payloadConfig.getInput().getQuotationReference());
            String planSelected = getPlanSelected(policyQuotaInternalId);
            payloadStore.setPolicyQuotaInternalIdList(policyQuotaInternalId);
            payloadStore.setPlanSelected(planSelected);
        }
        List<Long> plansToRimac = filterPlanIdToSendRimac(payloadStore.getPlanSelected(),payloadConfig.getPlansInformation());
        InsuranceEnterpriseInputBO rimacInput =createQuotationDAO(payloadConfig.getInput(),plansToRimac,payloadConfig.getCompanyQuotaId(),
                this.applicationConfigurationService);
        ConsumerExternalService consumerExternalService = new ConsumerExternalService();
        InsuranceEnterpriseResponseBO responseRimac = consumerExternalService.callRimacService(rimacInput,
                payloadConfig.getInput().getTraceId(),this.pisdR014,this.externalApiConnector);
        EnterpriseQuotationDTO response = mapInQuotationResponse(payloadConfig.getInput(),responseRimac,
                payloadConfig.getNextSimulationId(),payloadConfig.getPlansInformation(),payloadConfig.getCompanyQuotaId());
        FinancingBO financing = getFinancingBO(responseRimac.getPayload().getCotizaciones().get(0));
        ContactDetailsDTO contactMobile = getContactDetailByType(payloadConfig.getInput(), ConstantsUtil.ContactDetailtype.MOBILE);
        ContactDetailsDTO contactEmail = getContactDetailByType(payloadConfig.getInput(),ConstantsUtil.ContactDetailtype.EMAIL);
        String mobile = null;
        String email = null;
        if(contactMobile != null){
            mobile = contactMobile.getContact().getNumber();
        }

        if(contactEmail != null){
            email = contactEmail.getContact().getAddress();
        }

        if (financing != null) {
            payloadStore.setFechaInicio(financing.getFechaInicio());
            payloadStore.setFechaFin(financing.getFechaFin());
            payloadStore.setPremiumAmount(!CollectionUtils.isEmpty(financing.getCuotasFinanciamiento())?
                    financing.getCuotasFinanciamiento().get(ConstantsUtil.NumberConstants.ZERO).getMonto(): null);
           }
        if(payloadStore.getPolicyQuotaInternalIdList()!=null){
            String firstPolicyQuotaInternalId;
            firstPolicyQuotaInternalId = getFirstQuotationId(payloadStore.getPolicyQuotaInternalIdList());
            response.setId(generateSecondQuotationId(payloadConfig.getNextSimulationId(),payloadConfig.getInput(),payloadStore.getPolicyQuotaInternalIdList()));
            payloadStore.setFirstPolicyQuotaInternalId(firstPolicyQuotaInternalId);
        }
        payloadStore.setAddress(email);
        payloadStore.setNumber(mobile);
        payloadStore.setOutput(response);
        payloadStore.setRimacResponse(responseRimac);
        payloadStore.setPolicyQuotaInternalId(response.getId());
        payloadStore.setNextSimulationId(payloadConfig.getNextSimulationId());
        payloadStore.setInsuranceProductId(payloadConfig.getInsuranceProductId());
        LOGGER.info("***** InsrEnterpriseLifeBusinessImpl - doEnterpriseLife END | return payloadStore: {} *****", payloadStore);

        return payloadStore;
    }
    private static InsuranceEnterpriseInputBO createQuotationDAO(EnterpriseQuotationDTO quotationCreate,
                                                                List<Long> planList, String productName,ApplicationConfigurationService applicationConfigurationService){

        if(quotationCreate.getEmployees() == null){
            return null;
        }
        InsuranceEnterpriseInputBO payload = new InsuranceEnterpriseInputBO();
        QuotationInputBO companyQuotationPayloadBO = new QuotationInputBO();
        List<Long> firstPlanList = new ArrayList<>();
        companyQuotationPayloadBO.setProducto(productName);
        companyQuotationPayloadBO.setDatosParticulares(getDatosParticulares(quotationCreate.getEmployees()));
        companyQuotationPayloadBO.setPlanes(firstPlanList);

        if (isFirstCall(quotationCreate.getQuotationReference())) {
            companyQuotationPayloadBO.setTipoCotizacion(ConstantsUtil.StringConstants.R);
        } else {

            companyQuotationPayloadBO.setTipoCotizacion(ConstantsUtil.StringConstants.C);
            companyQuotationPayloadBO.setPlanes(planList);
            LOGGER.info("***** InsrEnterpriseLifeBusinessImpl - createQuotationDAO  |  planList: {} *****", planList);
        }

        String currency = quotationCreate.getEmployees().getMonthlyPayrollAmount().getCurrency();
        companyQuotationPayloadBO.setMoneda(currency);

        addContractorFromParticipant(quotationCreate, applicationConfigurationService, companyQuotationPayloadBO);

        payload.setPayload(companyQuotationPayloadBO);

        return payload;
    }
    private static void addContractorFromParticipant(EnterpriseQuotationDTO quotationCreate,
                                                     ApplicationConfigurationService applicationConfigurationService, QuotationInputBO companyQuotationPayloadBO) {

        if(!CollectionUtils.isEmpty(quotationCreate.getParticipants())){
            ParticipantDTO participantHolder = quotationCreate.getParticipants().stream()
                   //poner en variable
                    .filter(participant -> ConstantsUtil.StringConstants.PARTICIPANT_TYPE_HOLDER.equalsIgnoreCase(participant.getParticipantType().getId()))
                    .findFirst()
                    .orElse(null);

            if(participantHolder != null){
                ContractorDTO contractor = new ContractorDTO();
                contractor.setTipoDocumento(applicationConfigurationService.getProperty(
                        participantHolder.getIdentityDocument().getDocumentType().getId()));
                contractor.setNumeroDocumento(participantHolder.getIdentityDocument().getDocumentNumber());

                companyQuotationPayloadBO.setContratante(contractor);
            }
        }
    }

    public static List<ParticularDataBO> getDatosParticulares(EmployeesDTO employees){

        List<ParticularDataBO> particularData = new ArrayList<>();
        Double amount = employees.getMonthlyPayrollAmount().getAmount();
        DecimalFormat df = new DecimalFormat("#");
        String formattedAmount = df.format(amount);

        ParticularDataBO numeroTrabajadores = new ParticularDataBO();
        numeroTrabajadores.setEtiqueta(ContansUtils.rimacInput.NUMERO_DE_TRABAJADORES);
        numeroTrabajadores.setCodigo("");
        numeroTrabajadores.setValor(String.valueOf(employees.getEmployeesNumber()));
        particularData.add(numeroTrabajadores);

        ParticularDataBO indicadorEdadTrabajadores = new ParticularDataBO();
        indicadorEdadTrabajadores.setEtiqueta(ContansUtils.rimacInput.INDICADOR_EDAD_TRABAJADORES);
        //Agregar variables
        indicadorEdadTrabajadores.setValor(Boolean.TRUE.equals(employees.getAreMajorityAge()) ? "S" : "N");
        indicadorEdadTrabajadores.setCodigo(ContansUtils.StringsUtils.BLANK);
        particularData.add(indicadorEdadTrabajadores);

        ParticularDataBO planillaBrutaMensual = new ParticularDataBO();
        planillaBrutaMensual.setEtiqueta(ContansUtils.rimacInput.PLANILLA_BRUTA_MENSUAL);
        planillaBrutaMensual.setCodigo("");
        planillaBrutaMensual.setValor(formattedAmount);
        LOGGER.info("***** createQuotationDAO - getDatosParticulares  | argument formattedAmount: {} *****",
                formattedAmount);
        LOGGER.info("***** createQuotationDAO - getDatosParticulares  |  getAmount: {} *****",
                employees.getMonthlyPayrollAmount().getAmount().toString());
        particularData.add(planillaBrutaMensual);
        ParticularDataBO sumaAsegurada = new ParticularDataBO();
        sumaAsegurada.setValor(String.valueOf(10000));
        sumaAsegurada.setEtiqueta(ContansUtils.rimacInput.SUMA_ASEGURADA);
        sumaAsegurada.setCodigo("");
        particularData.add(sumaAsegurada);

        return particularData;
    }


    public static boolean isFirstCall(String quotationReference) {
        return Objects.isNull(quotationReference);
    }

    private static ContactDetailsDTO getContactDetailByType(EnterpriseQuotationDTO input, String detailType){
        ContactDetailsDTO contactDetailsDTO = null;

        if(input.getContactDetails() != null && !input.getContactDetails().isEmpty()){
            contactDetailsDTO = input.getContactDetails().stream()
                    .filter(contactDTO -> detailType.equals(contactDTO.getContact().getContactDetailType()))
                    .findFirst().orElse(null);
        }
        return contactDetailsDTO;
    }
    private static List<Long> filterPlanIdToSendRimac(String plans, List<InsuranceModalityDAO> planList) {
        List<Long> firstPlan = new ArrayList<>();
        LOGGER.info("***** InsrEnterpriseLifeBusinessImpl - filterPlanIdToSendRimac  |  plans: {} *****", plans);

        if (plans != null) {
            firstPlan=  planList.stream()
                    .filter(dto -> dto.getInsuranceModalityType().equals(plans))
                    .map(dto -> Long.parseLong(dto.getInsuranceCompanyModalityId()))
                    .collect(Collectors.toList());

        }
        LOGGER.info("***** InsrEnterpriseLifeBusinessImpl - filterPlanIdToSendRimac  |  firstPlan: {} *****", firstPlan);

        return firstPlan;
    }
    private EnterpriseQuotationDTO mapInQuotationResponse(EnterpriseQuotationDTO input,
                                                          InsuranceEnterpriseResponseBO payload, BigDecimal nextId,
                                                          List<InsuranceModalityDAO> planList,String productName) {

        QuotationResponseBO responseRimac = payload.getPayload();
        InsrEnterpriseLifeBusinessPlanImpl insrEnterpriseLifeBusinessPlan = new InsrEnterpriseLifeBusinessPlanImpl();
        input.getProduct().setPlans(!CollectionUtils.isEmpty(responseRimac.getCotizaciones())
                ? insrEnterpriseLifeBusinessPlan.getPlanInfo(listPlans(responseRimac.getCotizaciones()),this.applicationConfigurationService,
                planList,responseRimac.getDatosParticulares()) : null);
        LOGGER.info("***** InsrEnterpriseLifeBusinessImpl - mapInQuotationResponse  |  plans response: {} *****",  input.getProduct().getPlans());
        input.getProduct().setName(productName);
        input.setId(generateQuotationId(nextId, input));
        if(CollectionUtils.isEmpty(responseRimac.getCotizaciones())){
            input.setValidityPeriod(null);
        }
        else {
            input.setValidityPeriod(createValidityPeriodDTO(responseRimac.getCotizaciones().get(0).getFechaFinVigencia()));
        }
        input.setQuotationDate(LocalDate.now());


        return input;
    }
    private static FinancingBO getFinancingBO(QuotationBO quotationDetail) {
        PlanBO plan = quotationDetail.getPlan();
        FinancingBO firstFinancing = new FinancingBO();
        if (plan != null && plan.getFinanciamientos() != null) {
            firstFinancing = plan.getFinanciamientos().stream()
                    .filter(financingBO -> financingBO.getPeriodicidad().equalsIgnoreCase(ConstantsUtil.FinancingPeriodicity.ANUAL))
                    .findFirst().orElse(null);
        }
        return firstFinancing;
    }
    public List<String> getPolicyIdInfo(String rfkInternalId){
        LOGGER.info("***** executeCreateQuotation - getPolicyIdInfo START *****");

        IEnterprisePolicyQuotaInternalIdDAO enterprisePolicyQuotaInternalIdDAO = new EnterprisePolicyQuotaInternalIdDAO(pisdR402);
        List<String> policyQuotaInternalId = enterprisePolicyQuotaInternalIdDAO.getPolicyQuotaInternalId(rfkInternalId);

        LOGGER.info("***** executeCreateQuotation - getPolicyIdInfo | policyQuotaInternalId Value: {} *****",policyQuotaInternalId);
        return policyQuotaInternalId;
    }
    public String getPlanSelected(List<String> rfkInternalId){
        LOGGER.info("***** executeCreateQuotation - getPlanSelected START *****");

        IEnterprisePlanSelectedDAO enterprisePlanSelectedDAO = new EnterprisePlanSelectedDAO(pisdR402);
        String planSelected = enterprisePlanSelectedDAO.getPlanSelected(rfkInternalId);

        LOGGER.info("***** executeCreateQuotation - getPlanSelected | planSelected Value: {} *****",planSelected);
        return planSelected;
    }

    private ValidityPeriodDTO createValidityPeriodDTO(String fechaFinVigencia){
        //VALIDAR CAMPO FECHA FIN Y FECHA INICIO NO NULO
        ValidityPeriodDTO validityPeriodDTO = new ValidityPeriodDTO();
        validityPeriodDTO.setStartDate(convertLocalDateToDate(LocalDate.now()));
        validityPeriodDTO.setEndDate(convertLocalDateToDate(convertStringDateToLocalDate(fechaFinVigencia)));

        return validityPeriodDTO;
    }

    private LocalDate convertStringDateToLocalDate(String date){
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(ContansUtils.StringsUtils.DATE_FORMAT));
    }

    private Date convertLocalDateToDate(LocalDate date){
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static List<PlanBO> listPlans(List<QuotationBO> responseRimac) {
        List<PlanBO> planBOList = new ArrayList<>();

        for (QuotationBO quotationBO : responseRimac) {
            planBOList.add(quotationBO.getPlan());
        }
        return planBOList;
    }
    public static String generateQuotationId(BigDecimal nextId,EnterpriseQuotationDTO input){

        String simulationId = nextId.toString();
        String product = input.getProduct().getId();
        return input.getSourceBranchCode().concat(product).concat(simulationId).concat(ContansUtils.StringsUtils.LAST_DIGIT_00);
    }
    public static String generateSecondQuotationId(BigDecimal nextId,EnterpriseQuotationDTO input,List<String> policyIdList) {

        String simulationId = nextId.toString();
        String product = input.getProduct().getId();
        String lastDigits = new String();
        if (policyIdList.size() == 1) {
            lastDigits = ContansUtils.StringsUtils.LAST_DIGIT_01;
        } else if (policyIdList.size() > 1) {
            String subString = ContansUtils.StringsUtils.LAST_DIGIT_00;
            for (String policy : policyIdList) {
                if (!policy.substring(policy.length() - 2).equals(subString)) {
                    int intValueOfPolicy = Integer.parseInt(policy.substring(policy.length() - 2));
                    LOGGER.info("***** InsrEnterpriseLifeBusinessImpl - generateSecondQuotationId | intValueOfPolicy: {} *****", intValueOfPolicy);
                    int nextValue = intValueOfPolicy + 1;
                    String policyQuotaInternalNextId = ContansUtils.StringsUtils.ZERO.concat(String.valueOf(nextValue));
                    lastDigits = policyQuotaInternalNextId;
                    break;
                }
            }
        }
        return input.getSourceBranchCode().concat(product).concat(simulationId).concat(lastDigits);
    }
    public static String getFirstQuotationId(List<String> policyIdList) {

        String policyQuotaInternalId = new String();
        if (policyIdList.size() == 1) {
            policyQuotaInternalId = null;
        } else if (policyIdList.size() > 1) {
            String subString = ContansUtils.StringsUtils.LAST_DIGIT_00;
            for (String policy : policyIdList) {
                if (!policy.substring(policy.length() - 2).equals(subString)) {
                    policyQuotaInternalId = policy;
                    break;
                }
            }
        }
        return policyQuotaInternalId;
    }
}
