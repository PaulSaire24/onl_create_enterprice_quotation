package com.bbva.rbvd.lib.r403.transform.bean;

import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.dao.InsuranceProductDAO;
import com.bbva.rbvd.dto.enterpriseinsurance.utils.ConstantsUtil;

import java.math.BigDecimal;
import java.util.Map;

public class ProductBean {
    private ProductBean(){

    }

    public static InsuranceProductDAO getProductInformation(Map<String,Object> productMap){
        InsuranceProductDAO productInformationDAO = new InsuranceProductDAO();
        productInformationDAO.setInsuranceProductId((BigDecimal) productMap.get(ConstantsUtil.InsurancePrdModality.FIELD_INSURANCE_PRODUCT_ID));
        productInformationDAO.setProductShortDesc(String.valueOf(productMap.get(ConstantsUtil.InsuranceProduct.FIELD_PRODUCT_SHORT_DESC)));

        return productInformationDAO;
    }
}
