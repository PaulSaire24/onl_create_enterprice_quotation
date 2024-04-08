package com.bbva.rbvd.lib.r403.transform.map;

import com.bbva.rbvd.dto.enterpriseinsurance.utils.ConstantsUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class PolicyQuotaInternalIdMap {
    private PolicyQuotaInternalIdMap() {
    }

    public static Map<String, Object> createArgumentsForGetPolicyId(String rfkInternalId) {
        Map<String, Object> arguments = new HashMap<>();

        arguments.put(ConstantsUtil.QuotationMap.FIELD_RFQ_INTERNAL_ID, rfkInternalId);
        arguments.put(ConstantsUtil.QuotationMap.POLICY_QUOTA_INTERNAL_ID, rfkInternalId);

        return arguments;
    }
}
