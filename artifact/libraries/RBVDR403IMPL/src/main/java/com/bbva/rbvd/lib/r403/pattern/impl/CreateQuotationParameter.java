package com.bbva.rbvd.lib.r403.pattern.impl;

import com.bbva.pisd.lib.r401.PISDR401;
import com.bbva.pisd.lib.r402.PISDR402;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.EnterpriseQuotationDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.dao.InsuranceProductDAO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.dao.InsuranceModalityDAO;
import com.bbva.rbvd.lib.r403.pattern.PreCreateQuotation;
import com.bbva.rbvd.lib.r403.service.dao.IEnterprisePolicyQuotaInternalIdDAO;
import com.bbva.rbvd.lib.r403.service.dao.IEnterpriseProductDAO;
import com.bbva.rbvd.lib.r403.service.dao.IEnterprisePlanDAO;
import com.bbva.rbvd.lib.r403.service.dao.IInsuranceSimulationDAO;
import com.bbva.rbvd.lib.r403.service.dao.impl.EnterprisePolicyQuotaInternalIdDAO;
import com.bbva.rbvd.lib.r403.service.dao.impl.EnterpriseProductDAO;
import com.bbva.rbvd.lib.r403.service.dao.impl.EnterprisePlanDAO;
import com.bbva.rbvd.lib.r403.service.dao.impl.InsuranceSimulationDAOImpl;
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
        PayloadConfig payloadConfig = new PayloadConfig();
        InsuranceProductDAO product = getProductInfo(input.getProduct().getId());
        List<InsuranceModalityDAO> plans = getPlanInfo(product.getInsuranceProductId(),input.getSaleChannelId());
        BigDecimal nextId = this.getInsuranceSimulationId();

        if(!input.getQuotationReference().equals(null)) {
            List<String> policyQuotaInternalId = getPolicyIdInfo(input.getQuotationReference());
            payloadConfig.setPolicyQuotaInternalIdList(policyQuotaInternalId);
        }
        payloadConfig.setInput(input);
        payloadConfig.setProductInformationBySimulation(product);
        payloadConfig.setInsuranceProductId(product.getInsuranceProductId());
        payloadConfig.setCompanyQuotaId(product.getProductShortDesc());
        payloadConfig.setPlansInformation(plans);
        payloadConfig.setNextSimulationId(nextId);
        return payloadConfig;
    }
    public InsuranceProductDAO getProductInfo(String productTypeId){
        LOGGER.info("***** executeCreateQuotation - getProductInfo START *****");

        IEnterpriseProductDAO insuranceProductDAO = new EnterpriseProductDAO(pisdR401);
        InsuranceProductDAO product = insuranceProductDAO.getInsuranceProductId(productTypeId);

        LOGGER.info("***** executeCreateQuotation - getProductInfo | product Value: {} *****",product);
        return product;
    }
    public List<InsuranceModalityDAO> getPlanInfo(BigDecimal productTypeId, String Channel){
        LOGGER.info("***** executeCreateQuotation - getPlanInfo START *****");

        IEnterprisePlanDAO iEnterprisePlanDAO = new EnterprisePlanDAO(this.pisdR402);
        List<InsuranceModalityDAO> plansInfo = iEnterprisePlanDAO.getPlansId(productTypeId,Channel);
        return plansInfo;
    }
    public List<String> getPolicyIdInfo(String rfkInternalId){
        LOGGER.info("***** executeCreateQuotation - getProductInfo START *****");

        IEnterprisePolicyQuotaInternalIdDAO enterprisePolicyQuotaInternalIdDAO = new EnterprisePolicyQuotaInternalIdDAO(pisdR402);
        List<String> policyQuotaInternalId = enterprisePolicyQuotaInternalIdDAO.getPolicyQuotaInternalId(rfkInternalId);

        LOGGER.info("***** executeCreateQuotation - getPolicyIdInfo | policyQuotaInternalId Value: {} *****",policyQuotaInternalId);
        return policyQuotaInternalId;
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
