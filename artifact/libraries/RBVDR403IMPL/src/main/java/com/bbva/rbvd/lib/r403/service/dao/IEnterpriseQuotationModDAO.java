package com.bbva.rbvd.lib.r403.service.dao;

import java.math.BigDecimal;
import java.util.Map;

public interface IEnterpriseQuotationModDAO {
    void insertLifeEnterpriseQuotationMod(Map<String, Object> argumentForSaveParticipant);
    int executeDeleteQuotationMod(String policyQuotaInternalId, BigDecimal productId);


}
