package com.bbva.rbvd.lib.r403.service.dao.impl;

import com.bbva.pisd.lib.r401.PISDR401;
import com.bbva.rbvd.dto.enterpriseinsurance.utils.ConstantsUtil;
import com.bbva.rbvd.lib.r403.service.dao.IInsuranceProductDAO;

import java.util.Map;

public class InsuranceProductDAO implements IInsuranceProductDAO {
    private final PISDR401 pisdR401;
    public InsuranceProductDAO(PISDR401 pisdR401) {
        this.pisdR401 = pisdR401;
    }

    @Override
    public Map<String,Object> getInsuranceProductId(Map<String, Object> argumentsForGetProductId) {
        return (Map<String,Object>) this.pisdR401.executeGetProductById(
                ConstantsUtil.QueriesName.QUERY_SELECT_PRODUCT_BY_PRODUCT_TYPE,argumentsForGetProductId);
    }

}
