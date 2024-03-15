package com.bbva.rbvd.lib.r403.service.dao;


import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.*;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.rimac.AssistanceBO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.rimac.CoverageBO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.rimac.FinancingBO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.rimac.PlanBO;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

public class PlanDAO {

    //MEJORAR, EL APPLICATION PASAR POR SETTER O CONSTRUCTOR A ESTA CLASE
    public List<PlanDTO> getPlanInfo(List<PlanBO> planBOList, ApplicationConfigurationService applicationConfigurationService){
        if (Objects.isNull(planBOList)) {
            return Collections.emptyList();
        }
        addPlansId(planBOList);
        return planBOList.stream()
                .map(planBO -> {
                    PlanDTO planDTO = new PlanDTO();
                    planDTO.setId(planBO.getPlan().toString());
                    planDTO.setIsSelected(false);
                    planDTO.setName(planBO.getDescripcionPlan());
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
                    coverageDTO.setDescription(coverageBO.getObservacionCobertura());
                    coverageDTO.setName(coverageBO.getDescripcionCobertura());
                    return coverageDTO;
                })
                .collect(Collectors.toList());
    }
    private static void addPlansId(List<PlanBO> planBOList) {

        Map<Long, Long> planMap = new HashMap<>();
        planMap.put(534254L, 01l);
        planMap.put(534273L, 03l);
        planMap.put(534272L, 02l);

        // Asignar valores específicos a los objetos PlanBO basados en su campo plan
        planBOList.forEach(planBO -> {
            Long planValue = planBO.getPlan();
            if (planMap.containsKey(planValue)) {
                planBO.setPlan(planMap.get(planValue));
            }
        })
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
            rimacPlan.getCuotasFinanciamiento().get(0).setMoneda("P");
        }
        else{
            rimacPlan.getCuotasFinanciamiento().get(0).setMoneda("D");
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
