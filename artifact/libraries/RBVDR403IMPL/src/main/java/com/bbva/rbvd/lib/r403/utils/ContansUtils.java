package com.bbva.rbvd.lib.r403.utils;

import java.math.BigDecimal;

public class ContansUtils {
public static final class mockData{
      private mockData(){}
    public static final String COTIZACION="cdc9aac6-747f-47fe-bc41-8a2fd87cb36c";
    public static final String DIAS_VIGENCIA="30";
    public static final Long PLAN= Long.valueOf(277303);
    public static final Long ASISTENCIA= Long.valueOf(4313);
    public static final String DESCRIPTION_ASISTENCIA= "Cursos en Riesgos Laborales";
    public static final String FECHA_FIN_VIGENCIA= "2021-02-02";
    public static final BigDecimal PRIMA_NETA= BigDecimal.valueOf(577.2);
    public static final String DESCRIPTION_PLAN= "Plan Journey Vida Addons Soles";
    public static final Long FINANCIAMIENTO= Long.valueOf(24281);
    public static final String PERIODICIDAD= "Mensual y Anual";
    public static final Long NUMERO_CUOTAS= Long.valueOf(12);
    public static final String FECHA_INICIO= "2023-12-31";
    public static final Long CUOTA= Long.valueOf(1);
    public static final BigDecimal MONTO= BigDecimal.valueOf(701.53);
    public static final String FECHA_VENCIMIENTO= "2024-01-31";
    public static final Long COBERTURAS= Long.valueOf(9675);
    public static final String DESCRIPCION_COBERTURA= "Muerte Natural";
    public static final BigDecimal PRIMA_NETA_COBERTURA= BigDecimal.valueOf(19.12);
    public static final String MONEDA= "PEN";
    public static final BigDecimal SUMA_ASEGURADA= BigDecimal.valueOf(5519.12);
    public static final String OBSERVACION_COBERTURA= "Covertura Total";
    public static final String CONDITION= "Cobertura Total";
    public static final String PRODUCTO= "VIDALEY";
    public static final String ETIQUETA= "SumaAsegurada";
    public static final String CODIGO= "Ramo";
    public static final String VALOR= "265000";
}
    public static final class mockInternalData{
        private mockInternalData(){}
        public static final String BUSINESS_AGENT_NAME="NOMBRE AGENTE";
        public static final String PARTICIPANT_FIRST_NAME="ALEC";
        public static final String PARTICIPANT_LAST_NAME="TABOADA";
        public static final String PARTICIPANT_SECOND_LAST_NAME="TANIGUCHI";
        public static final String PRODUCT_NAME="VIDALEY";

    }
    public static final class rimacInput{
        private rimacInput(){}

        public static final String ETIQUETA_1="NUMERO_DE_TRABAJADORES";
        public static final String ETIQUETA_2="INDICADOR_EDAD_TRABAJADORES";
        public static final String ETIQUETA_3="PLANILLA_BRUTA_MENSUAL";
        public static final String ETIQUETA_4="SUMA_ASEGURADA";
        public static final int AMOUNT=30000;
        public static final String CURRENCY="PEN";

    }
    public static final class Querys{
        private Querys(){}
        public static final String INSERT_INSRNC_SIMLT_PRD_ENTERPRICE="PISD.INSERT_INSRNC_SIMLT_PRD_ENTERPRISE";
        public static final String INSERT_SIMULATE_ENTERPRICE = "PISD.INSERT_SIMULATE_ENTERPRISE";
        public static final String CURRENCY="PEN";
        public static final String URI_RIMAC_CREARCOTIZACION="/api-vida/V1/cotizaciones";

        public static final String FIELD_Q_PISD_SIMULATION_ID0_NEXTVAL="NEXTVAL";
    }
    public static final class Mapper{
        private Mapper(){}
        public static final String FIELD_INSURANCE_SIMULATION_ID="INSURANCE_SIMULATION_ID";
        public static final String FIELD_INSRNC_COMPANY_SIMULATION_ID = "INSRNC_COMPANY_SIMULATION_ID";
        public static final String FIELD_CUSTOMER_ID="CUSTOMER_ID";
        public static final String FIELD_CUSTOMER_SIMULATION_DATE="CUSTOMER_SIMULATION_DATE";
        public static final String FIELD_CUST_SIMULATION_EXPIRED_DATE = "CUST_SIMULATION_EXPIRED_DATE";
        public static final String FIELD_BANK_FACTOR_TYPE="BANK_FACTOR_TYPE";
        public static final String FIELD_BANK_FACTOR_AMOUNT="BANK_FACTOR_AMOUNT";
        public static final String FIELD_REGISTRY_SITUATION_TYPE="REGISTRY_SITUATION_TYPE";
        public static final String FIELD_BANK_FACTOR_PER = "BANK_FACTOR_PER";
        public static final String FIELD_SOURCE_BRANCH_ID="SOURCE_BRANCH_ID";
        public static final String FIELD_CREATION_USER_ID="CREATION_USER_ID";
        public static final String FIELD_USER_AUDIT_ID = "USER_AUDIT_ID";
        public static final String FIELD_PERSONAL_DOC_TYPE="PERSONAL_DOC_TYPE";
        public static final String FIELD_PARTICIPANT_PERSONAL_ID="PARTICIPANT_PERSONAL_ID";
        public static final String FIELD_INSURED_CUSTOMER_NAME = "INSURED_CUSTOMER_NAME";
        public static final String FIELD_CLIENT_LAST_NAME="CLIENT_LAST_NAME";
        public static final String FIELD_CUSTOMER_SEGMENT_NAME="CUSTOMER_SEGMENT_NAME";
        public static final String FIELD_OR_FILTER_INSURANCE_PRODUCT_ID = "INSURANCE_PRODUCT_ID";
        public static final String FIELD_CAMPAIGN_FACTOR_TYPE="CAMPAIGN_FACTOR_TYPE";
        public static final String FIELD_CAMPAIGN_OFFER_1_AMOUNT="CAMPAIGN_OFFER_1_AMOUNT";
        public static final String FIELD_CAMPAIGN_FACTOR_PER = "CAMPAIGN_FACTOR_PER";
        public static final String FIELD_SALE_CHANNEL_ID="SALE_CHANNEL_ID";

    }

}
