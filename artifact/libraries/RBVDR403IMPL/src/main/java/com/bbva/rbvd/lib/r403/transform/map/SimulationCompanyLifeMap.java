package com.bbva.rbvd.lib.r403.transform.map;

import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.EnterpriseQuotationDTO;

import com.bbva.rbvd.dto.enterpriseinsurance.utils.ConstantsUtil;
import com.bbva.rbvd.lib.r403.transfer.PayloadStore;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class SimulationCompanyLifeMap {
    private SimulationCompanyLifeMap(){}

    public static Map<String, Object> argumentsToSaveSimuCompanyLife(BigDecimal nextId,EnterpriseQuotationDTO responseDTO, PayloadStore payloadStore, BigDecimal producId){
        Map<String, Object> quoteCoLifeMap = new HashMap<>();

        quoteCoLifeMap.put(ConstantsUtil.QuotationMap.INSURANCE_SIMULATION_ID,nextId);
        quoteCoLifeMap.put(ConstantsUtil.QuotationModMap.INSURANCE_PRODUCT_ID,producId);
        quoteCoLifeMap.put(ConstantsUtil.InsuranceQuoteCoLife.FIELD_INCOMES_PAYROLL_AMOUNT,responseDTO.getEmployees().getMonthlyPayrollAmount().getAmount());
        quoteCoLifeMap.put(ConstantsUtil.InsuranceQuoteCoLife.FIELD_CURRENCY_ID,responseDTO.getEmployees().getMonthlyPayrollAmount().getCurrency());
        quoteCoLifeMap.put(ConstantsUtil.InsuranceQuoteCoLife.FIELD_PAYROLL_EMPLOYEE_NUMBER,responseDTO.getEmployees().getEmployeesNumber());
        quoteCoLifeMap.put(ConstantsUtil.InsuranceQuoteCoLife.FIELD_EMPLOYEE_EMAIL_NAME,payloadStore.getAddress());
        quoteCoLifeMap.put(ConstantsUtil.InsuranceQuoteCoLife.FIELD_AGE_EMPLOYEES_IND_TYPE,responseDTO.getEmployees().getAreMajorityAge());
        quoteCoLifeMap.put(ConstantsUtil.QuotationMap.CREATION_USER_ID,responseDTO.getCreationUser());
        quoteCoLifeMap.put(ConstantsUtil.QuotationMap.USER_AUDIT_ID,responseDTO.getUserAudit());

        return quoteCoLifeMap;
    }
}
