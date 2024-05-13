package com.bbva.rbvd.lib.r403.transform.map;

import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.EnterpriseQuotationDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.modifyquotation.dao.InsuranceQuoteCoLifeDAO;
import com.bbva.rbvd.dto.enterpriseinsurance.utils.ConstantsUtil;
import com.bbva.rbvd.lib.r403.transfer.PayloadStore;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class QuoteCompanyLifeMap {
    private QuoteCompanyLifeMap(){}

    public static Map<String, Object> argumentsToSaveQuoteCompanyLife(EnterpriseQuotationDTO responseDTO, PayloadStore payloadStore,BigDecimal producId){
        Map<String, Object> quoteCoLifeMap = new HashMap<>();

        quoteCoLifeMap.put(ConstantsUtil.QuotationMap.POLICY_QUOTA_INTERNAL_ID,responseDTO.getId());
        quoteCoLifeMap.put(ConstantsUtil.QuotationModMap.INSURANCE_PRODUCT_ID,producId);
        quoteCoLifeMap.put(ConstantsUtil.InsurancePrdModality.FIELD_INSURANCE_MODALITY_TYPE,payloadStore.getPlanSelected());
        quoteCoLifeMap.put(ConstantsUtil.InsuranceQuoteCoLife.FIELD_INCOMES_PAYROLL_AMOUNT,responseDTO.getEmployees().getMonthlyPayrollAmount().getAmount());
        quoteCoLifeMap.put(ConstantsUtil.InsuranceQuoteCoLife.FIELD_CURRENCY_ID,responseDTO.getEmployees().getMonthlyPayrollAmount().getCurrency());
        quoteCoLifeMap.put(ConstantsUtil.InsuranceQuoteCoLife.FIELD_PAYROLL_EMPLOYEE_NUMBER,responseDTO.getEmployees().getEmployeesNumber());
        quoteCoLifeMap.put(ConstantsUtil.InsuranceQuoteCoLife.FIELD_EMPLOYEE_EMAIL_NAME,payloadStore.getAddress());
        quoteCoLifeMap.put(ConstantsUtil.InsuranceQuoteCoLife.FIELD_AGE_EMPLOYEES_IND_TYPE,responseDTO.getEmployees().getAreMajorityAge());
        quoteCoLifeMap.put(ConstantsUtil.QuotationMap.CREATION_USER_ID,responseDTO.getCreationUser());
        quoteCoLifeMap.put(ConstantsUtil.QuotationMap.USER_AUDIT_ID,responseDTO.getUserAudit());

        return quoteCoLifeMap;
    }
    public static Map<String, Object> argumentsToDeleteQuoteCoLife(String policyQuotaInternalId,BigDecimal insuranceProductId) {
        Map<String, Object> argumentsToDelete = new HashMap<>();
        argumentsToDelete.put(ConstantsUtil.QuotationMap.POLICY_QUOTA_INTERNAL_ID,policyQuotaInternalId);
        argumentsToDelete.put(ConstantsUtil.QuotationModMap.INSURANCE_PRODUCT_ID, insuranceProductId);

        return argumentsToDelete;
    }
}
