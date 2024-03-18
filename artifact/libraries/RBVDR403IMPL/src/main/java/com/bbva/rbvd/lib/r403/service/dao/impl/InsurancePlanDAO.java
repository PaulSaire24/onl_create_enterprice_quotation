package com.bbva.rbvd.lib.r403.service.dao.impl;

import com.bbva.pisd.lib.r402.PISDR402;
import com.bbva.rbvd.dto.enterpriseinsurance.utils.ConstantsUtil;
import com.bbva.rbvd.lib.r403.service.dao.IInsurancePlanDAO;
import com.bbva.rbvd.lib.r403.service.dao.IInsuranceProductDAO;
import com.bbva.rbvd.lib.r403.service.dao.IInsuranceSimulationDAO;
import com.bbva.rbvd.lib.r403.utils.ContansUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InsurancePlanDAO implements IInsurancePlanDAO {
    private final PISDR402 pisdR402;

    public InsurancePlanDAO(PISDR402 pisdR402) {
        this.pisdR402 = pisdR402;
    }

    @Override
    public List<Map<String, Object>> getPlansId(Map<String, Object> argumentsForGetPlans) {
        return this.pisdR402.executeGetListASingleRow(ConstantsUtil.QueriesName.GET_MODALITY_TYPE_BY_PRODUCT_ID,argumentsForGetPlans);
    }}
