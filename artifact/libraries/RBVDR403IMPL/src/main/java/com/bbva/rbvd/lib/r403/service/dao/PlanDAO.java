package com.bbva.rbvd.lib.r403.service.dao;


import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.*;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.rimac.AssistanceBO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.rimac.CoverageBO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.rimac.FinancingBO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.rimac.PlanBO;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PlanDAO {

    public List<PlanDTO> getPlanInfo(List<PlanBO> planBOList){
        if (Objects.isNull(planBOList)) {
            return Collections.emptyList();
        }

        List<PlanDTO> planDTOList = planBOList.stream()
                .map(planBO -> {
                    PlanDTO planDTO = new PlanDTO();
                    planDTO.setId(planBO.getPlan().toString());
                    planDTO.setIsSelected(false);
                    planDTO.setIsRecommended(false);
                    planDTO.setInstallmentPlans(mapInstallmentPlans(planBO));
                    planDTO.setCoverages(mapCoverages(planBO));
                    planDTO.setTotalInstallment(mapTotalInstallmentPlans(planBO));
                    planDTO.setBenefits(mapBenefits(planBO));
                    return planDTO;
                })
                .collect(Collectors.toList());
        return planDTOList;
    }

    private static AmountDTO mapTotalInstallmentPlans(PlanBO rimacPlan) {
        if (Objects.isNull(rimacPlan)) {
            return null;
        }
        AmountDTO totalInstallmentPlan = new AmountDTO();
        totalInstallmentPlan.setAmount(rimacPlan.getPrimaNeta().doubleValue());
        totalInstallmentPlan.setCurrency(rimacPlan.getMoneda());
        return totalInstallmentPlan;
    }

    private static List<CoverageDTO> mapCoverages(PlanBO rimacPlan) {
        if (Objects.isNull(rimacPlan) || Objects.isNull(rimacPlan.getCoberturas())) {
            return Collections.emptyList();
        }
        List<CoverageBO> coverageBOList = rimacPlan.getCoberturas();
        List<CoverageDTO> coverageDTOList = coverageBOList.stream()
                .map(coverageBO -> {
                    CoverageDTO coverageDTO = new CoverageDTO();
                    coverageDTO.setCoverageType(mapCoverageType(coverageBO));
                    coverageDTO.setId(coverageBO.getCondicion());
                    coverageDTO.setDescription(coverageBO.getDescripcionCobertura());
                    return coverageDTO;
                })
                .collect(Collectors.toList());
        return coverageDTOList;
    }
    private static List<DescriptionDTO> mapBenefits(PlanBO rimacPlan) {
        if (Objects.isNull(rimacPlan) || Objects.isNull(rimacPlan.getAsistencias())) {
            return Collections.emptyList();
        }
        List<AssistanceBO> assistanceBOList =rimacPlan.getAsistencias();
        List<DescriptionDTO> benefitsDTOList = assistanceBOList.stream()
                .map(assistanceBO -> {
                    DescriptionDTO benefitsDTO = new DescriptionDTO();
                    benefitsDTO.setId(assistanceBO.getAsistencia().toString());
                    benefitsDTO.setName(assistanceBO.getDescripcionAsistencia());
                    return benefitsDTO;
                })
                .collect(Collectors.toList());
        return benefitsDTOList;
    }
    private static List<InstallmentPlansDTO> mapInstallmentPlans(PlanBO rimacPlan) {
        if (Objects.isNull(rimacPlan) || Objects.isNull(rimacPlan.getFinanciamientos())) {
            return Collections.emptyList();
        }
        List<FinancingBO> financingBOList = rimacPlan.getFinanciamientos();
        List<InstallmentPlansDTO> installmentPlansDTOList = financingBOList.stream()
                .map(financingBO -> {
                    InstallmentPlansDTO installmentPlansDTO = new InstallmentPlansDTO();
                    installmentPlansDTO.setPaymentsTotalNumber(financingBO.getNumeroCuotas());
                    installmentPlansDTO.setPaymentAmount(mapPaymentAmount(financingBO));
                    installmentPlansDTO.setPeriod(mapPeriod(financingBO));
                    return installmentPlansDTO;
                })
                .collect(Collectors.toList());

        return installmentPlansDTOList;
    }
    private static AmountDTO mapPaymentAmount(FinancingBO rimacPlan) {
        if (Objects.isNull(rimacPlan)) {
            return null;
        }
        AmountDTO paymentAmount = new AmountDTO();
        paymentAmount.setAmount(rimacPlan.getCuotasFinanciamiento().get(0).getMonto().doubleValue());
       //CAMBIAR
        if(rimacPlan.getCuotasFinanciamiento().get(0).getMoneda().equals("PEN")|| rimacPlan.getCuotasFinanciamiento().get(0).getMoneda().equals("SOL")) {
            paymentAmount.setCurrency("P");
        }
        else{
            paymentAmount.setCurrency("D");
        }
        return paymentAmount;
    }
    private static DescriptionDTO mapCoverageType(CoverageBO rimacPlan) {
        if (Objects.isNull(rimacPlan)) {
            return null;
        }
        DescriptionDTO coverageType = new DescriptionDTO();
        String condicion = getCondicionforCoverageType(rimacPlan);
        coverageType.setId(condicion);

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
    private static DescriptionDTO mapPeriod(FinancingBO rimacPlan) {
        DescriptionDTO period = new DescriptionDTO();
        String name;
        if (Objects.isNull(rimacPlan)) {
            return null;
        }
        if(rimacPlan.getPeriodicidad().equals("A")){
            name = "ANNUAL";
        }
        else{
            name = "MONTHLY";
        }

        period.setId(rimacPlan.getPeriodicidad());
        period.setName(name);

        return period;
    }
}
