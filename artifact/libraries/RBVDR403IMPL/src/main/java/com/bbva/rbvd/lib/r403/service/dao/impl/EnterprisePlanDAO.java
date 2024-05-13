package com.bbva.rbvd.lib.r403.service.dao.impl;

import com.bbva.pisd.lib.r402.PISDR402;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.dao.InsuranceModalityDAO;
import com.bbva.rbvd.dto.enterpriseinsurance.utils.ConstantsUtil;
import com.bbva.rbvd.lib.r403.service.dao.IEnterprisePlanDAO;
import com.bbva.rbvd.lib.r403.transform.bean.PlanBean;
import com.bbva.rbvd.lib.r403.transform.map.PlansMap;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class EnterprisePlanDAO implements IEnterprisePlanDAO {
    private final PISDR402 pisdR402;

    public EnterprisePlanDAO(PISDR402 pisdR402) {
        this.pisdR402 = pisdR402;
    }

    @Override
    public List<InsuranceModalityDAO> getPlansId(BigDecimal productId, String channel) {
        Map<String, Object> argumentsForGetPlansId = PlansMap.createArgumentsForGetPlansId(
                channel,productId);
        List<Map<String,Object>> productResponse= this.pisdR402.executeGetListASingleRow(ConstantsUtil.QueriesName.GET_MODALITY_TYPE_BY_PRODUCT_ID,argumentsForGetPlansId);

        return PlanBean.getPlanInformation(productResponse);
    }}
