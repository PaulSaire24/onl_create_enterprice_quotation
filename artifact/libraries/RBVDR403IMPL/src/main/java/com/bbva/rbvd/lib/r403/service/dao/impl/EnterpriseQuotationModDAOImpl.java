package com.bbva.rbvd.lib.r403.service.dao.impl;

import com.bbva.pisd.lib.r402.PISDR402;
import com.bbva.rbvd.dto.enterpriseinsurance.utils.ConstantsUtil;
import com.bbva.rbvd.dto.enterpriseinsurance.utils.RBVDErrors;
import com.bbva.rbvd.dto.enterpriseinsurance.utils.RBVDValidation;
import com.bbva.rbvd.lib.r403.service.dao.IEnterpriseQuotationModDAO;
import com.bbva.rbvd.lib.r403.transform.map.QuotationModMap;

import java.math.BigDecimal;
import java.util.Map;

public class EnterpriseQuotationModDAOImpl implements IEnterpriseQuotationModDAO {

    private final PISDR402 pisdR402;
    public EnterpriseQuotationModDAOImpl(PISDR402 pisdR402) {
        this.pisdR402 = pisdR402;
    }


    @Override
    public void insertLifeEnterpriseQuotationMod(Map<String, Object> quotationModMap){
        int saveQuotationMod = this.pisdR402.executeInsertSingleRow(ConstantsUtil.QueriesName.QUERY_INSERT_INSURANCE_QUOTATION_MOD,quotationModMap);

        if(saveQuotationMod != ConstantsUtil.NumberConstants.ONE){
            throw RBVDValidation.build(RBVDErrors.ERROR_QUOTATION_MOD_SAVING);
        }
    }
    @Override
    public int executeDeleteQuotationMod(String policyQuotaInternalId, BigDecimal productId) {
        return this.pisdR402.executeInsertSingleRow(ConstantsUtil.QueriesName.QUERY_DELETE_QUOTATION_MOD, QuotationModMap.argumentsToDeleteQuotationMod(policyQuotaInternalId,productId));
    }

}
