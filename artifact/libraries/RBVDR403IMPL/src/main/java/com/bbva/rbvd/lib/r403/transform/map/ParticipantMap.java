package com.bbva.rbvd.lib.r403.transform.map;

import com.bbva.rbvd.dto.enterpriseinsurance.commons.rimac.PlanBO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ParticipantMap {
    private ParticipantMap(){}
    public static List<PlanBO> mapPlan(List<Long> planList) {
        List<PlanBO> planBOList =new ArrayList<>();
        PlanBO planBO = new PlanBO();
      //  planBO.setPlan(getPlanSelected(planList));
        planBOList.add(planBO);
        return planBOList;
    }
    public static Long getPlanSelected(List<Map<String,Object>> planList){
        Long planSelected = null;
        if(Objects.requireNonNull(planList).equals(planList) && !planList.isEmpty()) {
             planSelected = (Long) planList.get(0).get("INSURANCE_MODALITY_ID");
        }
        return planSelected;
    }

    }

