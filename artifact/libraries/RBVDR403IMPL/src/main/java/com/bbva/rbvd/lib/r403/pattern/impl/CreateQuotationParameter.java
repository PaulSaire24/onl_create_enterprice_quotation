package com.bbva.rbvd.lib.r403.pattern.impl;

import com.bbva.pisd.lib.r401.PISDR401;
import com.bbva.pisd.lib.r402.PISDR402;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.EnterpriseQuotationDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.dao.InsuranceProductDAO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.dao.InsuranceModalityDAO;
import com.bbva.rbvd.lib.r403.pattern.PreCreateQuotation;
import com.bbva.rbvd.lib.r403.service.ProductService;
import com.bbva.rbvd.lib.r403.service.dao.*;
import com.bbva.rbvd.lib.r403.service.dao.impl.*;
import com.bbva.rbvd.lib.r403.transfer.PayloadConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

public class CreateQuotationParameter implements PreCreateQuotation {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateQuotationParameter.class);
    private final PISDR401 pisdR401;
    private final PISDR402 pisdR402;

    public CreateQuotationParameter(PISDR401 pisdR401,
                                    PISDR402 pisdR402) {
        this.pisdR401 = pisdR401;
        this.pisdR402 = pisdR402;
    }
    @Override
    public PayloadConfig getConfig(EnterpriseQuotationDTO input) {
        LOGGER.info("***** CreateQuotationParameter getConfig START *****");
        LOGGER.info("***** CreateQuotationParameter getConfig - input : {} *****",input);
        ProductService productService = new ProductService(pisdR401,pisdR402);
        PayloadConfig payloadConfig = new PayloadConfig();
        InsuranceProductDAO product = productService.getProductInfo(input.getProduct().getId());
        List<InsuranceModalityDAO> plans = productService.getPlanFromDB(product.getInsuranceProductId(),input.getSaleChannelId());
        BigDecimal nextId = this.getInsuranceSimulationId();


        payloadConfig.setInput(input);
        payloadConfig.setProductInformationBySimulation(product);
        payloadConfig.setCompanyQuotaId(product.getProductShortDesc());
        payloadConfig.setPlansFromBD(plans);
        payloadConfig.setNextSimulationId(nextId);
        return payloadConfig;
    }
    public BigDecimal getInsuranceSimulationId(){
        LOGGER.info("***** executeCreateQuotation - getInsuranceSimulationId START *****");

        IInsuranceSimulationDAO insuranceSimulationDao= new InsuranceSimulationDAOImpl(pisdR402);
        BigDecimal simulationNextValue = insuranceSimulationDao.getSimulationNextVal();

        LOGGER.info("***** executeCreateQuotation - getInsuranceSimulationId | simulationNextValue: {} *****",simulationNextValue);
        return simulationNextValue;
    }
    public static final class Builder {
        private PISDR401 pisdR401;
        private PISDR402 pisdR402;

        private Builder() {
        }

        public static Builder an() {
            return new Builder();
        }

        public Builder withPisdR401(PISDR401 pisdR401) {
            this.pisdR401 = pisdR401;
            return this;
        }

        public Builder withPisdR402(PISDR402 pisdR402) {
            this.pisdR402 = pisdR402;
            return this;
        }

        public CreateQuotationParameter build() {
            return new CreateQuotationParameter(pisdR401, pisdR402);
        }
    }
}
