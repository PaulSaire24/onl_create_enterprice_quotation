package com.bbva.rbvd.lib.r403.service.dao;


import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.*;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.rimac.AssistanceBO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.rimac.CoverageBO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.rimac.FinancingBO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.rimac.PlanBO;
import com.bbva.rbvd.lib.r403.utils.ContansUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Objects;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PlanDAO {

    //MEJORAR, EL APPLICATION PASAR POR SETTER O CONSTRUCTOR A ESTA CLASE
    public List<PlanDTO> getPlanInfo(List<PlanBO> planBOList, ApplicationConfigurationService applicationConfigurationService, List<Map<String, Object>> planList){
        if (Objects.isNull(planBOList)) {
            return Collections.emptyList();
        }
        addPlansId(planBOList,planList);
        return planBOList.stream()
                .map(planBO -> {
                    String[] plaName = planBO.getDescripcionPlan().split(" ");
                    String planName = null;
                    if (plaName.length >= 2) {
                       planName = plaName[0] + " " + plaName[1];
                        }
                    PlanDTO planDTO = new PlanDTO();
                    planDTO.setId(String.format("%02d", planBO.getPlan()));
                    planDTO.setIsSelected(false);
                    planDTO.setName(planName);
                    planDTO.setIsRecommended(false);
                    planDTO.setInstallmentPlans(mapInstallmentPlans(planBO,applicationConfigurationService));
                    planDTO.setCoverages(mapCoverages(planBO,applicationConfigurationService));
                    planDTO.setTotalInstallment(mapTotalInstallmentPlans(planBO));
                    planDTO.setBenefits(mapBenefits(planBO));
                    return planDTO;
                })
                .collect(Collectors.toList());
    }

    private static AmountDTO mapTotalInstallmentPlans(PlanBO rimacPlan) {
        if (Objects.isNull(rimacPlan.getPrimaNeta()) || Objects.isNull(rimacPlan.getMoneda())) {
            return null;
        }
        if(rimacPlan.getMoneda().equals("PEN")||
                rimacPlan.getMoneda().equals("SOL")){
            rimacPlan.setMoneda("PEN");
        }
        else{
            rimacPlan.setMoneda("USD");
        }
        AmountDTO totalInstallmentPlan = new AmountDTO();
        totalInstallmentPlan.setAmount(rimacPlan.getPrimaNeta().doubleValue());
        totalInstallmentPlan.setCurrency(rimacPlan.getMoneda());

        return totalInstallmentPlan;
    }

    private static List<CoverageDTO> mapCoverages(PlanBO rimacPlan,ApplicationConfigurationService applicationConfigurationService) {
        if (CollectionUtils.isEmpty(rimacPlan.getCoberturas())) {
            return Collections.emptyList();
        }

        return rimacPlan.getCoberturas().stream()
                .map(coverageBO -> {
                    CoverageDTO coverageDTO = new CoverageDTO();
                    coverageDTO.setCoverageType(mapCoverageType(coverageBO,applicationConfigurationService));
                    coverageDTO.setId(coverageBO.getCobertura().toString());
                    coverageDTO.setDescription(coverageBO.getNumeroSueldos()+ContansUtils.rimacInput.REMUNERACIONES);
                    coverageDTO.setName(coverageBO.getObservacionCobertura());
                    return coverageDTO;
                })
                .collect(Collectors.toList());
    }
    private static void addPlansId(List<PlanBO> planBOList,List<Map<String, Object>> planList) {

        Map<String, String> idToTypeMap = planList.stream()
                .collect(Collectors.toMap(
                        map -> (String) map.get("INSURANCE_COMPANY_MODALITY_ID"),
                        map -> (String) map.get("INSURANCE_MODALITY_TYPE")));

        // Update the PlanBO objects using the map
        planBOList.forEach(planBO -> planBO.setPlan(Long.parseLong(idToTypeMap.getOrDefault(planBO.getPlan(), null))));

        ;
    }
    private static List<DescriptionDTO> mapBenefits(PlanBO rimacPlan) {
        if (CollectionUtils.isEmpty(rimacPlan.getAsistencias())) {
            return Collections.emptyList();
        }

        List<AssistanceBO> assistanceBOList =rimacPlan.getAsistencias();
        return assistanceBOList.stream()
                .map(assistanceBO -> {
                    DescriptionDTO benefitsDTO = new DescriptionDTO();

                    benefitsDTO.setId(assistanceBO.getAsistencia().toString());
                    benefitsDTO.setName(assistanceBO.getDescripcionAsistencia());

                    return benefitsDTO;
                })
                .collect(Collectors.toList());
    }
    private static List<InstallmentPlansDTO> mapInstallmentPlans(PlanBO rimacPlan,ApplicationConfigurationService applicationConfigurationService) {
        if (CollectionUtils.isEmpty(rimacPlan.getFinanciamientos())) {
            return Collections.emptyList();
        }
        List<FinancingBO> financingBOList = rimacPlan.getFinanciamientos();

        return financingBOList.stream()
                .map(financingBO -> {
                    InstallmentPlansDTO installmentPlansDTO = new InstallmentPlansDTO();
                    installmentPlansDTO.setPaymentsTotalNumber(financingBO.getNumeroCuotas());
                    installmentPlansDTO.setPaymentAmount(mapPaymentAmount(financingBO));
                    installmentPlansDTO.setPeriod(mapPeriod(financingBO,applicationConfigurationService));
                    return installmentPlansDTO;
                })
                .collect(Collectors.toList());
    }
    private static AmountDTO mapPaymentAmount(FinancingBO rimacPlan) {
        if (Objects.isNull(rimacPlan)) {
            return null;
        }

        AmountDTO paymentAmount = new AmountDTO();
        paymentAmount.setAmount(rimacPlan.getCuotasFinanciamiento().get(0).getMonto().doubleValue());
        if(rimacPlan.getCuotasFinanciamiento().get(0).getMoneda().equals("PEN")||
                rimacPlan.getCuotasFinanciamiento().get(0).getMoneda().equals("SOL")){
            rimacPlan.getCuotasFinanciamiento().get(0).setMoneda("PEN");
        }
        else{
            rimacPlan.getCuotasFinanciamiento().get(0).setMoneda("USD");
        }
        paymentAmount.setCurrency(rimacPlan.getCuotasFinanciamiento().get(0).getMoneda());

        return paymentAmount;
    }
    private static DescriptionDTO mapCoverageType(CoverageBO coverageBO,ApplicationConfigurationService applicationConfigurationService) {
        if (Objects.isNull(coverageBO.getCondicion())) {
            return null;
        }

        DescriptionDTO coverageType = new DescriptionDTO();
        String condition = applicationConfigurationService.getProperty("COVERAGE_TYPE_" + coverageBO.getCondicion());
        coverageType.setId(condition);
        coverageType.setName(condition);

        return coverageType;
    }
    private static String getCondicionforCoverageType(CoverageBO rimacPlan) {

        String condicion =null;
        if(rimacPlan.getCondicion().equals("OBL")){
            condicion = "MAIN";
        }
        else if(rimacPlan.getCondicion().equals("INC")){
            condicion = "INCLUDED";
        }
        else if(rimacPlan.getCondicion().equals("OPC")){
            condicion = "ADDITIONAL";
        }
        return condicion;
    }
    private static DescriptionDTO mapPeriod(FinancingBO rimacPlan,ApplicationConfigurationService applicationConfigurationService) {
        DescriptionDTO period = new DescriptionDTO();

        if (Objects.isNull(rimacPlan.getPeriodicidad())) {
            return null;
        }

        period.setId(applicationConfigurationService.getProperty("PERIODICITY_ID_" + rimacPlan.getPeriodicidad()));
        period.setName(applicationConfigurationService.getProperty("PERIODICITY_NAME_" + rimacPlan.getPeriodicidad()));

        return period;
    }
}
