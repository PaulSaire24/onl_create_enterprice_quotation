package com.bbva.rbvd.lib.r403.transform.bean;

import com.bbva.rbvd.dto.enterpriseinsurance.utils.ConstantsUtil;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlanSelectedBean {
    private PlanSelectedBean(){

    }

    public static String getPlanSelected(Map<String,Object> policyQuotaInternalIdMap){
                    String policyQuotaId;
                    policyQuotaId = (String) policyQuotaInternalIdMap.get(ConstantsUtil.InsurancePrdModality.FIELD_INSURANCE_MODALITY_TYPE);
                    return policyQuotaId;
                }


}
