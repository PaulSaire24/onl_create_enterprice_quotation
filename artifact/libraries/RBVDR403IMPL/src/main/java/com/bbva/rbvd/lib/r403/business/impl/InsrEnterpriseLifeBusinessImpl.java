package com.bbva.rbvd.lib.r403.business.impl;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.elara.utility.api.connector.APIConnector;
import com.bbva.pisd.lib.r014.PISDR014;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.EnterpriseQuotationDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.ValidityPeriodDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.rimac.PlanBO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.dao.InsuranceModalityDAO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.InsuranceEnterpriseInputBO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.InsuranceEnterpriseResponseBO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.QuotationBO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.QuotationResponseBO;
import com.bbva.rbvd.lib.r403.business.IInsrEnterpriseLifeBusiness;
import com.bbva.rbvd.lib.r403.service.impl.ConsumerExternalService;
import com.bbva.rbvd.lib.r403.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r403.transfer.PayloadStore;
import com.bbva.rbvd.lib.r403.transform.bean.QuotationBean;
import com.bbva.rbvd.lib.r403.transform.list.IListEnterprisePlan;
import com.bbva.rbvd.lib.r403.transform.list.impl.ListEnterprisePlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class InsrEnterpriseLifeBusinessImpl implements IInsrEnterpriseLifeBusiness {
    private static final Logger LOGGER = LoggerFactory.getLogger(InsrEnterpriseLifeBusinessImpl.class);
    private final ApplicationConfigurationService applicationConfigurationService;
    private final APIConnector externalApiConnector;
    private final PISDR014 pisdR014;
    public InsrEnterpriseLifeBusinessImpl(ApplicationConfigurationService applicationConfigurationService,
                                          APIConnector externalApiConnector,PISDR014 pisdR014) {
        this.applicationConfigurationService = applicationConfigurationService;
        this.externalApiConnector = externalApiConnector;
        this.pisdR014 = pisdR014;
    }
    @Override
    public PayloadStore doEnterpriseLife(PayloadConfig payloadConfig) {
        LOGGER.info("***** InsrEnterpriseLifeBusinessImpl - doEnterpriseLife START | argument payloadConfig: {} *****",
                payloadConfig);

        PayloadStore payloadStore = new PayloadStore();
        List<Long> plansToRimac = filterRimacPlansId(payloadConfig.getPlansInformation());
        InsuranceEnterpriseInputBO rimacInput = QuotationBean.createQuotationDAO(payloadConfig.getInput(),plansToRimac,payloadConfig.getCompanyQuotaId(),
                this.applicationConfigurationService);
        ConsumerExternalService consumerExternalService = new ConsumerExternalService();
        InsuranceEnterpriseResponseBO responseRimac = consumerExternalService.callRimacService(rimacInput,
                payloadConfig.getInput().getTraceId(),this.pisdR014,this.externalApiConnector);
        EnterpriseQuotationDTO response = mapInQuotationResponse(payloadConfig.getInput(),responseRimac,
                payloadConfig.getNextSimulationId(),payloadConfig.getPlansInformation(),payloadConfig.getCompanyQuotaId());
        if(!payloadConfig.getPolicyQuotaInternalIdList().isEmpty() && payloadConfig.getPolicyQuotaInternalIdList()!=null){
           String firstPolicyQuotaInternalId;
            firstPolicyQuotaInternalId = getFirstQuotationId(payloadConfig.getPolicyQuotaInternalIdList());
            response.setId(generateSecondQuotationId(payloadConfig.getNextSimulationId(),payloadConfig.getInput(),payloadConfig.getPolicyQuotaInternalIdList()));
            payloadStore.setFirstPolicyQuotaInternalId(firstPolicyQuotaInternalId);
        }

        payloadStore.setOutput(response);
        payloadStore.setRimacResponse(responseRimac);
        payloadStore.setPolicyQuotaInternalId(response.getId());
        payloadStore.setNextSimulationId(payloadConfig.getNextSimulationId());
        payloadStore.setInsuranceProductId(payloadConfig.getInsuranceProductId());
        LOGGER.info("***** InsrEnterpriseLifeBusinessImpl - doEnterpriseLife END | return payloadStore: {} *****", payloadStore);

        return payloadStore;
    }
    private static List<Long> filterRimacPlansId(List<InsuranceModalityDAO> plans) {
        List<Long> planIds;
         planIds = plans.stream()
                .map(dto -> Long.parseLong(dto.getInsuranceCompanyModalityId()))
                .collect(Collectors.toList());
        return planIds;
    }
    private EnterpriseQuotationDTO mapInQuotationResponse(EnterpriseQuotationDTO input,
                                                          InsuranceEnterpriseResponseBO payload, BigDecimal nextId,
                                                          List<InsuranceModalityDAO> planList,String productName) {

        QuotationResponseBO responseRimac = payload.getPayload();
        IListEnterprisePlan planDAO = new ListEnterprisePlan(this.applicationConfigurationService);

        input.getProduct().setPlans(!CollectionUtils.isEmpty(responseRimac.getCotizaciones())
                ? planDAO.getPlanInfo(listPlans(responseRimac.getCotizaciones()),this.applicationConfigurationService,
                planList) : null);
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

    private ValidityPeriodDTO createValidityPeriodDTO(String fechaFinVigencia){
        //VALIDAR CAMPO FECHA FIN Y FECHA INICIO NO NULO
        ValidityPeriodDTO validityPeriodDTO = new ValidityPeriodDTO();
        validityPeriodDTO.setStartDate(convertLocalDateToDate(LocalDate.now()));
        validityPeriodDTO.setEndDate(convertLocalDateToDate(convertStringDateToLocalDate(fechaFinVigencia)));

        return validityPeriodDTO;
    }

    private LocalDate convertStringDateToLocalDate(String date){
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
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
        return input.getSourceBranchCode().concat(product).concat(simulationId).concat("00");
    }
    public static String generateSecondQuotationId(BigDecimal nextId,EnterpriseQuotationDTO input,List<String> policyIdList) {

        String simulationId = nextId.toString();
        String product = input.getProduct().getId();
        String lastDigits = new String();
        if (policyIdList.size() == 1) {
            lastDigits = "01";
        } else if (policyIdList.size() > 1) {
            String subString = "00";
            for (String policy : policyIdList) {
                if (!policy.substring(policy.length() - 2).equals(subString)) {
                    int intValueOfPolicy = Integer.parseInt(policy.substring(policy.length() - 2));
                    int nextValue = intValueOfPolicy + 1;
                    String policyQuotaInternalNextId = String.valueOf(nextValue);
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
            String subString = "00";
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
