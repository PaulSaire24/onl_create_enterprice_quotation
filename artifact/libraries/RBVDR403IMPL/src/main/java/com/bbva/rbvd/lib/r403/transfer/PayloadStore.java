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
    private String planSelected;
    private String fechaInicio;
    private String fechaFin;
    private BigDecimal premiumAmount;
    private String address;
    private String number;

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

    public String getPlanSelected() {
        return planSelected;
    }

    public void setPlanSelected(String planSelected) {
        this.planSelected = planSelected;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public BigDecimal getPremiumAmount() {
        return premiumAmount;
    }

    public void setPremiumAmount(BigDecimal premiumAmount) {
        this.premiumAmount = premiumAmount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
