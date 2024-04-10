package com.bbva.rbvd.lib.r403.transfer;

import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.EnterpriseQuotationDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.dao.InsuranceModalityDAO;

import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.dao.InsuranceProductDAO;

import java.math.BigDecimal;
import java.util.List;

public class PayloadConfig {
    private EnterpriseQuotationDTO input;
    private String companyQuotaId;
    private InsuranceProductDAO productInformationBySimulation;
    private BigDecimal insuranceProductId;
    private List<InsuranceModalityDAO> plansInformation;
    private List<String> policyQuotaInternalIdList;
    private BigDecimal nextSimulationId;
    private String planSelected;
    public EnterpriseQuotationDTO getInput() {
        return input;
    }

    public void setInput(EnterpriseQuotationDTO input) {
        this.input = input;
    }

    public String getCompanyQuotaId() {
        return companyQuotaId;
    }

    public void setCompanyQuotaId(String companyQuotaId) {
        this.companyQuotaId = companyQuotaId;
    }

    public InsuranceProductDAO getProductInformationBySimulation() {
        return productInformationBySimulation;
    }

    public void setProductInformationBySimulation(InsuranceProductDAO productInformationBySimulation) {
        this.productInformationBySimulation = productInformationBySimulation;
    }

    public BigDecimal getInsuranceProductId() {
        return insuranceProductId;
    }

    public void setInsuranceProductId(BigDecimal insuranceProductId) {
        this.insuranceProductId = insuranceProductId;
    }

    public List<InsuranceModalityDAO> getPlansInformation() {
        return plansInformation;
    }

    public void setPlansInformation(List<InsuranceModalityDAO> plansInformation) {
        this.plansInformation = plansInformation;
    }

    public List<String> getPolicyQuotaInternalIdList() {
        return policyQuotaInternalIdList;
    }

    public void setPolicyQuotaInternalIdList(List<String> policyQuotaInternalIdList) {
        this.policyQuotaInternalIdList = policyQuotaInternalIdList;
    }

    public BigDecimal getNextSimulationId() {
        return nextSimulationId;
    }

    public void setNextSimulationId(BigDecimal nextSimulationId) {
        this.nextSimulationId = nextSimulationId;
    }

    public String getPlanSelected() {
        return planSelected;
    }

    public void setPlanSelected(String planSelected) {
        this.planSelected = planSelected;
    }
}
