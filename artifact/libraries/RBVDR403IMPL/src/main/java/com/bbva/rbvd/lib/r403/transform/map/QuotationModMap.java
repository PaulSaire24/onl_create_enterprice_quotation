package com.bbva.rbvd.lib.r403.transform.map;

import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.EnterpriseQuotationDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.modifyquotation.dao.InsuranceQuotationModDAO;
import com.bbva.rbvd.dto.enterpriseinsurance.utils.ConstantsUtil;
import com.bbva.rbvd.lib.r403.transfer.PayloadStore;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class QuotationModMap {

        private QuotationModMap(){}

        public static Map<String, Object> argumentsToSaveQuotationMod(EnterpriseQuotationDTO responseDTO, PayloadStore payloadStore, BigDecimal producId){
            Map<String, Object> quotationModMap = new HashMap<>();

            quotationModMap.put(ConstantsUtil.QuotationModMap.POLICY_QUOTA_INTERNAL_ID,responseDTO.getId());
            quotationModMap.put(ConstantsUtil.QuotationModMap.INSURANCE_PRODUCT_ID,producId);
            quotationModMap.put(ConstantsUtil.InsurancePrdModality.FIELD_INSURANCE_MODALITY_TYPE,payloadStore.getPlanSelected());
            quotationModMap.put(ConstantsUtil.QuotationModMap.SALE_CHANNEL_ID,responseDTO.getSaleChannelId());
            if ( !responseDTO.getProduct().getPlans().isEmpty() &&
                    responseDTO.getProduct().getPlans().get(0).getInstallmentPlans() != null &&
                    !responseDTO.getProduct().getPlans().get(0).getInstallmentPlans().isEmpty()&&
                    responseDTO.getProduct().getPlans().get(0).getTotalInstallment() != null) {
            quotationModMap.put(ConstantsUtil.QuotationModMap.PAYMENT_TERM_NUMBER,responseDTO.getProduct().getPlans().get(0).getInstallmentPlans().get(0).getPaymentsTotalNumber());
            quotationModMap.put(ConstantsUtil.QuotationModMap.POLICY_PAYMENT_FREQUENCY_TYPE,responseDTO.getProduct().getPlans().get(0).getInstallmentPlans().get(0).getPeriod());
            quotationModMap.put(ConstantsUtil.QuotationModMap.PREMIUM_CURRENCY_ID,responseDTO.getProduct().getPlans().get(0).getTotalInstallment().getCurrency());
            } else {
                quotationModMap.put(ConstantsUtil.QuotationModMap.PAYMENT_TERM_NUMBER,null);
                quotationModMap.put(ConstantsUtil.QuotationModMap.POLICY_PAYMENT_FREQUENCY_TYPE,null);
                quotationModMap.put(ConstantsUtil.QuotationModMap.PREMIUM_CURRENCY_ID,null);
            }
            quotationModMap.put(ConstantsUtil.QuotationModMap.FINANCING_START_DATE,payloadStore.getFechaInicio());
            quotationModMap.put(ConstantsUtil.QuotationModMap.FINANCING_END_DATE,payloadStore.getFechaFin());
            quotationModMap.put(ConstantsUtil.QuotationModMap.PREMIUM_AMOUNT,payloadStore.getPremiumAmount());
            quotationModMap.put(ConstantsUtil.QuotationModMap.SAVED_QUOTATION_IND_TYPE,"S");
            quotationModMap.put(ConstantsUtil.QuotationModMap.LAST_CHANGE_BRANCH_ID,responseDTO.getLastChangeBranchId());
            quotationModMap.put(ConstantsUtil.QuotationModMap.SOURCE_BRANCH_ID,responseDTO.getLastChangeBranchId());
            quotationModMap.put(ConstantsUtil.QuotationModMap.CREATION_USER_ID,responseDTO.getCreationUser());
            quotationModMap.put(ConstantsUtil.QuotationModMap.USER_AUDIT_ID,responseDTO.getUserAudit());
            quotationModMap.put(ConstantsUtil.QuotationModMap.CONTACT_EMAIL_DESC,payloadStore.getAddress());
            quotationModMap.put(ConstantsUtil.QuotationModMap.CUSTOMER_PHONE_DESC,payloadStore.getNumber());
            quotationModMap.put(ConstantsUtil.QuotationModMap.DATA_TREATMENT_IND_TYPE,responseDTO.getDataTreatment());

            return quotationModMap;
        }
        public static Map<String, Object> argumentsToDeleteQuotationMod(String policyQuotaInternalId,BigDecimal insuranceProductId) {
            Map<String, Object> argumentsToDelete = new HashMap<>();
            argumentsToDelete.put(ConstantsUtil.QuotationModMap.POLICY_QUOTA_INTERNAL_ID,policyQuotaInternalId);
            argumentsToDelete.put(ConstantsUtil.QuotationModMap.INSURANCE_PRODUCT_ID, insuranceProductId);

            return argumentsToDelete;
        }
    public static Map<String, Object> argumentsToGetPlanSelected(String policyQuotaInternalId) {
        Map<String, Object> argumentsToDelete = new HashMap<>();
        argumentsToDelete.put(ConstantsUtil.QuotationModMap.POLICY_QUOTA_INTERNAL_ID,policyQuotaInternalId);

        return argumentsToDelete;
    }
}
