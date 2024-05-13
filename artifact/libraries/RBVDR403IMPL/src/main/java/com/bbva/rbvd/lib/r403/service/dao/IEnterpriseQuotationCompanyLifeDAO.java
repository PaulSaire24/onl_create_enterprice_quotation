package com.bbva.rbvd.lib.r403.service.dao;

import java.math.BigDecimal;
import java.util.Map;

public interface IEnterpriseQuotationCompanyLifeDAO {
    void insertLifeEnterpriseQuoteCompanyLife(Map<String, Object> argumentForSave);
    int executeDeleteQuoteCompanyLife(String policyQuotaInternalId, BigDecimal productId);

}
