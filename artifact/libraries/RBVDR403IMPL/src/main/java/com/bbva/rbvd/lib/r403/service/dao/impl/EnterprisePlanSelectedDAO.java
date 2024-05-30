package com.bbva.rbvd.lib.r403.service.dao.impl;

import com.bbva.pisd.lib.r402.PISDR402;
import com.bbva.rbvd.lib.r403.service.dao.IEnterprisePlanSelectedDAO;
import com.bbva.rbvd.lib.r403.service.dao.IEnterprisePolicyQuotaInternalIdDAO;
import com.bbva.rbvd.lib.r403.transform.bean.PlanSelectedBean;

import com.bbva.rbvd.lib.r403.transform.map.QuotationModMap;

import java.util.List;
import java.util.Map;

public class EnterprisePlanSelectedDAO implements IEnterprisePlanSelectedDAO {
    private final PISDR402 pisdR402;

    public EnterprisePlanSelectedDAO(PISDR402 pisdR402) {
        this.pisdR402 = pisdR402;
    }

    @Override
    public String getPlanSelected(List<String> rfkInternalId) {
        Map<String, Object> argumentsForGetPlanSelected= QuotationModMap.argumentsToGetPlanSelected(
                rfkInternalId.get(0));
        Map<String,Object> planSelected= this.pisdR402.executeGetASingleRow("PISD.SELECT_QUOTATION_BY_PLAN",argumentsForGetPlanSelected);

        return PlanSelectedBean.getPlanSelected(planSelected);
    }
}
