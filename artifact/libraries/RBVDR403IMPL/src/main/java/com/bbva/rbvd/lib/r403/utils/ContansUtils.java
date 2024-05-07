package com.bbva.rbvd.lib.r403.utils;

import java.math.BigDecimal;

public class ContansUtils {

    public static final class rimacInput{
        private rimacInput(){}
        public static final String CREATE_QUOTATION_API="enterprisequotation.create";

        public static final String NUMERO_DE_TRABAJADORES="NUMERO_DE_TRABAJADORES";
        public static final String INDICADOR_EDAD_TRABAJADORES="INDICADOR_EDAD_TRABAJADORES";
        public static final String PLANILLA_BRUTA_MENSUAL="PLANILLA_BRUTA_MENSUAL";
        public static final String REMUNERACIONES=" remuneraciones";
        public static final String GET_QUOTATION_POLICY_ID="PISD.GET_QUOTATION_POLICY_ID";
        public static final String INSERT_INSRNC_SIMLT_CO_LIF="PISD.INSERT_INSRNC_SIMLT_CO_LIF";
        public static final String DELETE_INSURED_QUOTATION_ENTERPRISE="PISD.DELETE_INSURED_QUOTATION_ENTERPRISE";
    }
    public static final class Uri{
        private Uri(){}
        public static final String URI_RIMAC_CREARCOTIZACION="/api-vida/V1/cotizaciones";

    }
    public static final class StringsUtils{
        private StringsUtils(){}
        public static final String BLANK = "";
        public static final String RUC = "RUC";
        public static final String AUTHORIZATION = "Authorization";
        public static final String X_AMZ_DATE = "X-Amz-Date";
        public static final String APPLICATION = "application";
        public static final String JSON = "json";
        public static final String X_API_KEY = "x-api-key";
        public static final String TRACE_ID = "traceId";
        public static final String DATE_FORMAT = "yyyy-MM-dd";
        public static final String LAST_DIGIT_00 = "00";
        public static final String LAST_DIGIT_01 = "01";

        public static final String ZERO = "0";
        public static final String AMOUNT_PEN = "PEN";
        public static final String AMOUNT_SOL = "SOL";
        public static final String AMOUNT_USD = "USD";
        public static final String COVERAGE_TYPE = "COVERAGE_TYPE_";
        public static final String PERIODICITY_ID = "PERIODICITY_ID_";
        public static final String PERIODICITY_NAME = "PERIODICITY_NAME_";
        public static final String SUMA_ASEGURADA = "SUMA_ASEGURADA";
    }

}
