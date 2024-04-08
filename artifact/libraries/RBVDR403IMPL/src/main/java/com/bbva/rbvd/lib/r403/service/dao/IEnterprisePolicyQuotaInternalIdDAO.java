package com.bbva.rbvd.lib.r403.service.dao;

import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.dao.InsuranceQuotationDAO;

import java.math.BigDecimal;
import java.util.List;

public interface IEnterprisePolicyQuotaInternalIdDAO {
        List<InsuranceQuotationDAO> getPolicyQuotaInternalId(String rfkInternalId);
}
