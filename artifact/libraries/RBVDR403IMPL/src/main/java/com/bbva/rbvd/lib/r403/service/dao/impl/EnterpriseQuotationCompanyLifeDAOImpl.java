package com.bbva.rbvd.lib.r403.service.dao.impl;

import com.bbva.pisd.lib.r402.PISDR402;
import com.bbva.rbvd.dto.enterpriseinsurance.utils.ConstantsUtil;
import com.bbva.rbvd.dto.enterpriseinsurance.utils.RBVDErrors;
import com.bbva.rbvd.dto.enterpriseinsurance.utils.RBVDValidation;
import com.bbva.rbvd.lib.r403.service.dao.IEnterpriseQuotationCompanyLifeDAO;
import com.bbva.rbvd.lib.r403.transform.map.QuoteCompanyLifeMap;


import java.math.BigDecimal;
import java.util.Map;

public class EnterpriseQuotationCompanyLifeDAOImpl implements IEnterpriseQuotationCompanyLifeDAO {
    private final PISDR402 pisdR402;

    public EnterpriseQuotationCompanyLifeDAOImpl(PISDR402 pisdR402) {
        this.pisdR402 = pisdR402;
    }

    @Override
    public void insertLifeEnterpriseQuoteCompanyLife(Map<String, Object> quotationQuoteCoLife) {
        int saveQuoteCoLife = this.pisdR402.executeInsertSingleRow(ConstantsUtil.QueriesName.QUERY_INSERT_INSRNC_QUOTE_CO_LIFE,quotationQuoteCoLife );

        if(saveQuoteCoLife != ConstantsUtil.NumberConstants.ONE){
            throw RBVDValidation.build(RBVDErrors.ERROR_QUOTATION_MOD_SAVING);
        }
    }

    @Override
    public int executeDeleteQuoteCompanyLife(String policyQuotaInternalId, BigDecimal productId) {
        return this.pisdR402.executeInsertSingleRow(ConstantsUtil.QueriesName.QUERY_DELETE_INSRNC_QUOTE_CO_LIFE, QuoteCompanyLifeMap.argumentsToDeleteQuoteCoLife(policyQuotaInternalId,productId));
    }
}
