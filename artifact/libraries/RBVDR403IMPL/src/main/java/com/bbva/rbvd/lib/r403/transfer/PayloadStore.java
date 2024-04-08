package com.bbva.rbvd.lib.r403.transfer;

import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.EnterpriseQuotationDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.InsuranceEnterpriseResponseBO;

import java.math.BigDecimal;

public class PayloadStore {
    private EnterpriseQuotationDTO output;
    private InsuranceEnterpriseResponseBO rimacResponse;
    private String policyQuotaInternalId;
    private BigDecimal nextSimulationId;
    private BigDecimal insuranceProductId;
    private String firstPolicyQuotaInternalId;
    public EnterpriseQuotationDTO getOutput() {
        return output;
    }

    public void setOutput(EnterpriseQuotationDTO output) {
        this.output = output;
    }

    public InsuranceEnterpriseResponseBO getRimacResponse() {
        return rimacResponse;
    }

    public void setRimacResponse(InsuranceEnterpriseResponseBO rimacResponse) {
        this.rimacResponse = rimacResponse;
    }

    public String getPolicyQuotaInternalId() {
        return policyQuotaInternalId;
    }

    public void setPolicyQuotaInternalId(String policyQuotaInternalId) {
        this.policyQuotaInternalId = policyQuotaInternalId;
    }

    public BigDecimal getNextSimulationId() {
        return nextSimulationId;
    }

    public void setNextSimulationId(BigDecimal nextSimulationId) {
        this.nextSimulationId = nextSimulationId;
    }

    public BigDecimal getInsuranceProductId() {
        return insuranceProductId;
    }

    public void setInsuranceProductId(BigDecimal insuranceProductId) {
        this.insuranceProductId = insuranceProductId;
    }

    public String getFirstPolicyQuotaInternalId() {
        return firstPolicyQuotaInternalId;
    }

    public void setFirstPolicyQuotaInternalId(String firstPolicyQuotaInternalId) {
        this.firstPolicyQuotaInternalId = firstPolicyQuotaInternalId;
    }
}
