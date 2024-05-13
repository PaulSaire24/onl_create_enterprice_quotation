package com.bbva.rbvd.lib.r403.transform.map;

import com.bbva.rbvd.dto.enterpriseinsurance.utils.ConstantsUtil;

import java.util.HashMap;
import java.util.Map;

public class QuotationDeleteMap {
    public static Map<String, Object> createArgumentsForDeleteQuotation(String policyQuotaInternalId) {
        Map<String, Object> arguments = new HashMap<>();

        arguments.put(ConstantsUtil.QuotationMap.POLICY_QUOTA_INTERNAL_ID, policyQuotaInternalId);


        return arguments;
    }
}
