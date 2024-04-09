package com.bbva.rbvd.lib.r403.service.dao.impl;

import com.bbva.pisd.lib.r402.PISDR402;



import com.bbva.rbvd.lib.r403.service.dao.IEnterprisePolicyQuotaInternalIdDAO;
import com.bbva.rbvd.lib.r403.transform.bean.PolicyQuotaInternalIdBean;
import com.bbva.rbvd.lib.r403.transform.map.PolicyQuotaInternalIdMap;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class EnterprisePolicyQuotaInternalIdDAO implements IEnterprisePolicyQuotaInternalIdDAO {
    private final PISDR402 pisdR402;

    public EnterprisePolicyQuotaInternalIdDAO(PISDR402 pisdR402) {
        this.pisdR402 = pisdR402;
    }

    @Override
    public List<String> getPolicyQuotaInternalId(String rfkInternalId) {
        Map<String, Object> argumentsForGetPlansId = PolicyQuotaInternalIdMap.createArgumentsForGetPolicyId(
                rfkInternalId);
        List<Map<String,Object>> productResponse= this.pisdR402.executeGetListASingleRow("PISD.GET_QUOTATION_POLICY_ID",argumentsForGetPlansId);

        return PolicyQuotaInternalIdBean.getPolicyQuotaInternalId(productResponse);
    }
}
