package com.bbva.rbvd.lib.r403.utils;

import java.math.BigDecimal;

public class ContansUtils {

    public static final class rimacInput{
        private rimacInput(){}

        public static final String ETIQUETA_1="NUMERO_DE_TRABAJADORES";
        public static final String ETIQUETA_2="INDICADOR_EDAD_TRABAJADORES";
        public static final String ETIQUETA_3="PLANILLA_BRUTA_MENSUAL";
        public static final String ETIQUETA_4="SUMA_ASEGURADA";

    }
    public static final class Querys{
        private Querys(){}
        public static final String INSERT_INSRNC_SIMLT_PRD_ENTERPRICE="PISD.INSERT_INSRNC_SIMLT_PRD_ENTERPRISE";
        public static final String INSERT_SIMULATE_ENTERPRICE = "PISD.INSERT_SIMULATE_ENTERPRISE";
        public static final String INSERT_INSURANCE_QUOTATION = "PISD.INSERT_INSURANCE_QUOTATION";

        public static final String GET_MODALITY_TYPE_BY_PRODUCT_ID = "PISD.GET_MODALITY_TYPE_BY_PRODUCT_ID";

        public static final String URI_RIMAC_CREARCOTIZACION="/api-vida/V1/cotizaciones";

        public static final String FIELD_Q_PISD_SIMULATION_ID0_NEXTVAL="NEXTVAL";
    }
    public static final class Mapper {
        private Mapper() {
        }

        public static final String FIELD_INSURANCE_SIMULATION_ID = "INSURANCE_SIMULATION_ID";
        public static final String FIELD_INSRNC_COMPANY_SIMULATION_ID = "INSRNC_COMPANY_SIMULATION_ID";
        public static final String FIELD_CUSTOMER_ID = "CUSTOMER_ID";
        public static final String FIELD_CUSTOMER_SIMULATION_DATE = "CUSTOMER_SIMULATION_DATE";
        public static final String FIELD_CUST_SIMULATION_EXPIRED_DATE = "CUST_SIMULATION_EXPIRED_DATE";
        public static final String FIELD_BANK_FACTOR_TYPE = "BANK_FACTOR_TYPE";
        public static final String FIELD_BANK_FACTOR_AMOUNT = "BANK_FACTOR_AMOUNT";
        public static final String FIELD_REGISTRY_SITUATION_TYPE = "REGISTRY_SITUATION_TYPE";
        public static final String FIELD_BANK_FACTOR_PER = "BANK_FACTOR_PER";
        public static final String FIELD_SOURCE_BRANCH_ID = "SOURCE_BRANCH_ID";
        public static final String FIELD_CREATION_USER_ID = "CREATION_USER_ID";
        public static final String FIELD_USER_AUDIT_ID = "USER_AUDIT_ID";
        public static final String FIELD_PERSONAL_DOC_TYPE = "PERSONAL_DOC_TYPE";
        public static final String FIELD_PARTICIPANT_PERSONAL_ID = "PARTICIPANT_PERSONAL_ID";
        public static final String FIELD_INSURED_CUSTOMER_NAME = "INSURED_CUSTOMER_NAME";
        public static final String FIELD_CLIENT_LAST_NAME = "CLIENT_LAST_NAME";
        public static final String FIELD_CUSTOMER_SEGMENT_NAME = "CUSTOMER_SEGMENT_NAME";
        public static final String FIELD_OR_FILTER_INSURANCE_PRODUCT_ID = "INSURANCE_PRODUCT_ID";
        public static final String FIELD_CAMPAIGN_FACTOR_TYPE = "CAMPAIGN_FACTOR_TYPE";
        public static final String FIELD_CAMPAIGN_OFFER_1_AMOUNT = "CAMPAIGN_OFFER_1_AMOUNT";
        public static final String FIELD_CAMPAIGN_FACTOR_PER = "CAMPAIGN_FACTOR_PER";
        public static final String FIELD_SALE_CHANNEL_ID = "SALE_CHANNEL_ID";

        public static final String FIELD_POLICY_QUOTA_INTERNAL_ID = "POLICY_QUOTA_INTERNAL_ID";
        public static final String FIELD_INSURANCE_COMPANY_QUOTA_ID = "INSURANCE_COMPANY_QUOTA_ID";
        public static final String FIELD_QUOTE_DATE = "QUOTE_DATE";
        public static final String FIELD_QUOTA_HMS_DATE = "QUOTA_HMS_DATE";
        public static final String FIELD_POLICY_QUOTA_END_VALIDITY_DATE = "POLICY_QUOTA_END_VALIDITY_DATE";
        public static final String FIELD_POLICY_QUOTA_STATUS_TYPE = "POLICY_QUOTA_STATUS_TYPE";
        public static final String FIELD_LAST_CHANGE_BRANCH_ID = "LAST_CHANGE_BRANCH_ID";
        public static final String FIELD_RFQ_INTERNAL_ID = "RFQ_INTERNAL_ID";
        public static final String FIELD_INSURANCE_PRODUCT_TYPE = "INSURANCE_PRODUCT_TYPE";
        public static final String FIELD_INSURANCE_PRODUCT_ID = "INSURANCE_PRODUCT_ID";

        public static final String FIELD_INSURANCE_COMPANY_MODALITY_ID = "INSURANCE_COMPANY_MODALITY_ID";
        public static final String FIELD_PRODUCT_SHORT_DESC = "PRODUCT_SHORT_DESC";
        public static final String FIELD_INSURANCE_MODALITY_TYPE = "INSURANCE_MODALITY_TYPE";

    }

}
