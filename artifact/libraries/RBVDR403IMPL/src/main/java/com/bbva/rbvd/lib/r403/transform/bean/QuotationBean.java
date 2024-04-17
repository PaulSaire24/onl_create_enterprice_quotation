package com.bbva.rbvd.lib.r403.transform.bean;


import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.*;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.rimac.ParticularDataBO;

import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.InsuranceEnterpriseInputBO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.QuotationInputBO;
import com.bbva.rbvd.lib.r403.utils.ContansUtils;
import org.springframework.util.CollectionUtils;


import java.text.DecimalFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class  QuotationBean {

    private QuotationBean(){}

    public static InsuranceEnterpriseInputBO createQuotationDAO(EnterpriseQuotationDTO quotationCreate,
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
            companyQuotationPayloadBO.setTipoCotizacion("R");
        } else {
            companyQuotationPayloadBO.setTipoCotizacion("C");
            companyQuotationPayloadBO.setPlanes(planList);
        }

        String currency = quotationCreate.getEmployees().getMonthlyPayrollAmount().getCurrency();
        companyQuotationPayloadBO.setMoneda(currency);

        contractorAddToRequestRimac(quotationCreate, applicationConfigurationService, companyQuotationPayloadBO);

        payload.setPayload(companyQuotationPayloadBO);

        return payload;
    }

    private static void contractorAddToRequestRimac(EnterpriseQuotationDTO quotationCreate,
                        ApplicationConfigurationService applicationConfigurationService, QuotationInputBO companyQuotationPayloadBO) {

        if(CollectionUtils.isEmpty(quotationCreate.getParticipants())){
            ParticipantDTO participantHolder = quotationCreate.getParticipants().stream()
                    .filter(participant -> "HOLDER".equalsIgnoreCase(participant.getParticipantType().getId()))
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
        Random random = new Random();

        // Generar un número aleatorio entre 0 y 1 (inclusive)
        int randomNumber = random.nextInt(2);

        // Asignar el valor según el número aleatorio generado
        int value;
        if (randomNumber == 0) {
            value = 10000;
        } else {
            value = 12000;
        }

        ParticularDataBO numeroTrabajadores = new ParticularDataBO();
        numeroTrabajadores.setEtiqueta(ContansUtils.rimacInput.ETIQUETA_1);
        numeroTrabajadores.setCodigo("");
        numeroTrabajadores.setValor(String.valueOf(employees.getEmployeesNumber()));
        particularData.add(numeroTrabajadores);

        ParticularDataBO indicadorEdadTrabajadores = new ParticularDataBO();
        indicadorEdadTrabajadores.setEtiqueta(ContansUtils.rimacInput.ETIQUETA_2);
        indicadorEdadTrabajadores.setValor(Boolean.TRUE.equals(employees.getAreMajorityAge()) ? "S" : "N");
        indicadorEdadTrabajadores.setCodigo("");
        particularData.add(indicadorEdadTrabajadores);

        ParticularDataBO planillaBrutaMensual = new ParticularDataBO();
        planillaBrutaMensual.setEtiqueta(ContansUtils.rimacInput.ETIQUETA_3);
        planillaBrutaMensual.setCodigo("");
        planillaBrutaMensual.setValor(formattedAmount);
        particularData.add(planillaBrutaMensual);
        System.out.println(formattedAmount);
        ParticularDataBO sumaAsegurada = new ParticularDataBO();
        sumaAsegurada.setValor(String.valueOf(value));
        sumaAsegurada.setEtiqueta(ContansUtils.rimacInput.ETIQUETA_4);
        sumaAsegurada.setCodigo("");
        particularData.add(sumaAsegurada);

        return particularData;
    }


     public static boolean isFirstCall(String quotationReference) {
         return Objects.isNull(quotationReference);
     }

}
