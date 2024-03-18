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
        public static final String FIELD_CUSTOMER_SIMULATION_DATE = "CUSTOMER_SIMULATION_DATE";
        public static final String FIELD_CUST_SIMULATION_EXPIRED_DATE = "CUST_SIMULATION_EXPIRED_DATE";
        public static final String FIELD_BANK_FACTOR_TYPE = "BANK_FACTOR_TYPE";
        public static final String FIELD_BANK_FACTOR_AMOUNT = "BANK_FACTOR_AMOUNT";
        public static final String FIELD_REGISTRY_SITUATION_TYPE = "REGISTRY_SITUATION_TYPE";
        public static final String FIELD_BANK_FACTOR_PER = "BANK_FACTOR_PER";
        public static final String FIELD_INSURED_CUSTOMER_NAME = "INSURED_CUSTOMER_NAME";
        public static final String FIELD_CLIENT_LAST_NAME = "CLIENT_LAST_NAME";
        public static final String FIELD_CUSTOMER_SEGMENT_NAME = "CUSTOMER_SEGMENT_NAME";
        public static final String FIELD_CAMPAIGN_FACTOR_TYPE = "CAMPAIGN_FACTOR_TYPE";
        public static final String FIELD_CAMPAIGN_OFFER_1_AMOUNT = "CAMPAIGN_OFFER_1_AMOUNT";
        public static final String FIELD_CAMPAIGN_FACTOR_PER = "CAMPAIGN_FACTOR_PER";
        public static final String FIELD_POLICY_QUOTA_STATUS_TYPE = "POLICY_QUOTA_STATUS_TYPE";
        public static final String FIELD_RFQ_INTERNAL_ID = "RFQ_INTERNAL_ID";

    }

}
