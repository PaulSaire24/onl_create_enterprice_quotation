package com.bbva.rbvd.lib.r403.service;

import com.bbva.pisd.lib.r401.PISDR401;
import com.bbva.pisd.lib.r402.PISDR402;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.dao.InsuranceModalityDAO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.dao.InsuranceProductDAO;
import com.bbva.rbvd.lib.r403.pattern.impl.CreateQuotationParameter;
import com.bbva.rbvd.lib.r403.service.dao.IEnterprisePlanDAO;
import com.bbva.rbvd.lib.r403.service.dao.IEnterpriseProductDAO;
import com.bbva.rbvd.lib.r403.service.dao.impl.EnterprisePlanDAO;
import com.bbva.rbvd.lib.r403.service.dao.impl.EnterpriseProductDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

public class ProductService {
    private final PISDR401 pisdR401;
    private final PISDR402 pisdR402;

    public ProductService(PISDR401 pisdR401,
                                    PISDR402 pisdR402) {
        this.pisdR401 = pisdR401;
        this.pisdR402 = pisdR402;
    }
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);
    public InsuranceProductDAO getProductInfo(String productTypeId){
        LOGGER.info("***** executeCreateQuotation - getProductInfo START *****");

        IEnterpriseProductDAO insuranceProductDAO = new EnterpriseProductDAO(pisdR401);
        InsuranceProductDAO product = insuranceProductDAO.getInsuranceProductId(productTypeId);

        LOGGER.info("***** executeCreateQuotation - getProductInfo | product Value: {} *****",product);
        return product;
    }
    public List<InsuranceModalityDAO> getPlanFromDB(BigDecimal productTypeId, String Channel){
        LOGGER.info("***** executeCreateQuotation - getPlanInfo START *****");

        IEnterprisePlanDAO iEnterprisePlanDAO = new EnterprisePlanDAO(this.pisdR402);
        List<InsuranceModalityDAO> plansInfo = iEnterprisePlanDAO.getPlansId(productTypeId,Channel);
        LOGGER.info("***** executeCreateQuotation - plansInfo: {}  ****",plansInfo);
        return plansInfo;
    }
}
