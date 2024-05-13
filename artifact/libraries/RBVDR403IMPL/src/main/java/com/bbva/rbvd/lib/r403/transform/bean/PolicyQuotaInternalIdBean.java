package com.bbva.rbvd.lib.r403.transform.bean;

import com.bbva.rbvd.dto.enterpriseinsurance.utils.ConstantsUtil;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PolicyQuotaInternalIdBean {
    private PolicyQuotaInternalIdBean(){

    }

    public static List<String> getPolicyQuotaInternalId(List<Map<String,Object>> policyQuotaInternalIdMap){
        return policyQuotaInternalIdMap.stream()
                .map(policyQuotaInternalId -> {
                  String policyQuotaId;
                    policyQuotaId = (String) policyQuotaInternalId.get(ConstantsUtil.QuotationMap.POLICY_QUOTA_INTERNAL_ID);
                    return policyQuotaId;
                })
                .collect(Collectors.toList());

    }
}
