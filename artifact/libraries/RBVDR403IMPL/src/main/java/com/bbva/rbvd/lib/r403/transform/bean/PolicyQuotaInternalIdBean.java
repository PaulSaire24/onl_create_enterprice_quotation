package com.bbva.rbvd.lib.r403.transform.bean;

import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.dao.InsuranceQuotationDAO;
import com.bbva.rbvd.dto.enterpriseinsurance.modifyquotation.dao.InsuranceProductDAO;
import com.bbva.rbvd.dto.enterpriseinsurance.utils.ConstantsUtil;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PolicyQuotaInternalIdBean {
    private PolicyQuotaInternalIdBean(){

    }

    public static List<InsuranceQuotationDAO> getPolicyQuotaInternalId(List<Map<String,Object>> policyQuotaInternalIdMap){
        return policyQuotaInternalIdMap.stream()
                .map(policyQuotaInternalId -> {
                    InsuranceQuotationDAO policyQuotaId = new InsuranceQuotationDAO();
                    policyQuotaId.setPolicyQuotaInternalId((String) policyQuotaInternalId.get(ConstantsUtil.QuotationMap.POLICY_QUOTA_INTERNAL_ID));
                    return policyQuotaId;
                })
                .collect(Collectors.toList());

    }
}
