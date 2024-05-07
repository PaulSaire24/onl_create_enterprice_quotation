package com.bbva.rbvd.lib.r403.business.impl;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.PlanDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.rimac.ParticularDataBO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.rimac.PlanBO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.dao.InsuranceModalityDAO;
import com.bbva.rbvd.lib.r403.impl.utils.ValidMaps;
import com.bbva.rbvd.lib.r403.transform.list.impl.ListEnterprisePlan;
import com.bbva.rbvd.lib.r403.utils.ContansUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class InsrEnterpriseLifeBusinessPlanImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(InsrEnterpriseLifeBusinessPlanImpl.class);

    public List<PlanDTO> getPlanInfo(List<PlanBO> planBOList, ApplicationConfigurationService applicationConfigurationService,
                                     List<InsuranceModalityDAO> planList, List<ParticularDataBO> datosParticulares){
        if (Objects.isNull(planBOList)) {
            return Collections.emptyList();
        }
        ListEnterprisePlan listEnterprisePlan = new ListEnterprisePlan(applicationConfigurationService);
        addPlansName(planBOList,planList);
        addPlansId(planBOList,planList);
        LOGGER.info("***** InsrEnterpriseLifeBusinessPlanImpl - getPlanInfo  |  datosParticulares response: {} *****",  datosParticulares);

        return planBOList.stream()
                .map(planBO -> {
                    String plaName = planBO.getDescripcionPlan();
                    PlanDTO planDTO = new PlanDTO();
                    planDTO.setId(String.format("%02d", planBO.getPlan()));
                    planDTO.setIsSelected(false);
                    planDTO.setName(plaName);
                    planDTO.setIsRecommended(false);
                    planDTO.setInstallmentPlans(listEnterprisePlan.mapInstallmentPlans(planBO,applicationConfigurationService));
                    planDTO.setCoverages(listEnterprisePlan.mapCoverages(datosParticulares,planBO,applicationConfigurationService));
                    planDTO.setTotalInstallment(listEnterprisePlan.mapTotalInstallmentPlans(planBO,applicationConfigurationService));
                    planDTO.setBenefits(listEnterprisePlan.mapBenefits(planBO));
                    return planDTO;
                })
                .collect(Collectors.toList());
    }
    private static void addPlansId(List<PlanBO> planBOList,List<InsuranceModalityDAO> planList) {

        Map<String, String> idToTypeMap = planList.stream()
                .collect(Collectors.toMap(
                        map -> map.getInsuranceCompanyModalityId(),
                        map -> map.getInsuranceModalityType()));
        if(!ValidMaps.mapIsNullOrEmpty(idToTypeMap)) {
            // Update the PlanBO objects using the map
            planBOList.forEach(planBO -> {
                String planId = planBO.getPlan().toString(); // Accede al atributo plan y conviértelo a cadena
                if (planId != null) {
                    // Convertir la cadena a int y luego a Long para eliminar los ceros a la izquierda
                    int intValue = Integer.parseInt(idToTypeMap.getOrDefault(planId, ContansUtils.StringsUtils.ZERO));
                    planBO.setPlan((long) intValue);
                }
            });
            planBOList.sort(Comparator.comparingLong(PlanBO::getPlan));
        }
    }
    private static void addPlansName(List<PlanBO> planBOList,List<InsuranceModalityDAO> planList) {

        Map<String, String> nameToIdMap = planList.stream()
                .collect(Collectors.toMap(
                        //PONER EN EL DTO LA CLAVE
                        map ->  map.getInsuranceCompanyModalityId(),
                        map ->  map.getInsuranceModalityName()));
        if(!ValidMaps.mapIsNullOrEmpty(nameToIdMap)) {
            // Actualiza PlanBO usando el mapa
            planBOList.forEach(planBO -> {
                String planName = planBO.getPlan().toString(); // Accede al atributo plan y conviértelo a cadena
                if (planName != null) {
                    // Convertir la cadena a int y luego a Long para eliminar los ceros a la izquierda
                    String intValue =nameToIdMap.getOrDefault(planName, ContansUtils.StringsUtils.ZERO);
                    planBO.setDescripcionPlan(intValue);
                }
            });
        }
    }

}
