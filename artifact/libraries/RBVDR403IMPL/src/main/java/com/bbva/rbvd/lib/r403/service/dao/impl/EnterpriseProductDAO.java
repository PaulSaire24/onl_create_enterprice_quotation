package com.bbva.rbvd.lib.r403.service.dao.impl;

import com.bbva.pisd.lib.r401.PISDR401;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.dao.InsuranceProductDAO;
import com.bbva.rbvd.dto.enterpriseinsurance.utils.ConstantsUtil;
import com.bbva.rbvd.lib.r403.service.dao.IEnterpriseProductDAO;

import com.bbva.rbvd.lib.r403.transform.bean.ProductBean;
import com.bbva.rbvd.lib.r403.transform.map.ProductMap;

import java.util.Map;

public class EnterpriseProductDAO implements IEnterpriseProductDAO {
    private final PISDR401 pisdR401;
    public EnterpriseProductDAO(PISDR401 pisdR401) {
        this.pisdR401 = pisdR401;
    }

    @Override
    public InsuranceProductDAO getInsuranceProductId(String argumentsForGetProductId) {
        Map<String,Object> paramsProduct = ProductMap.createArgumentsForGetProductId(
                argumentsForGetProductId);
         Map<String,Object> productResponse= (Map<String,Object>) this.pisdR401.executeGetProductById(
                ConstantsUtil.QueriesName.QUERY_SELECT_PRODUCT_BY_PRODUCT_TYPE,paramsProduct);
        return ProductBean.getProductInformation(productResponse);
    }

}
