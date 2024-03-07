package com.bbva.rbvd.lib.r403.utils;

public enum RBVDErrors{
    ERROR_FROM_RIMAC("RBVD00000119", false, "Error desde Rimac con la cotizacion enviada"),
    WRONG_PRODUCT_CODE("RBVD01020022", false, "El codigo de producto enviado es incorrecto"),
    WRONG_PLAN_CODES("RBVD01020023", false, "Los codigos de los planes son incorrectos"),
    INSERTION_ERROR_IN_SIMULATION_TABLE("RBVD00120019", false, "Parametros requeridos incompletos para el registro de simulacion"),
    INSERTION_ERROR_IN_SIMULATION_PRD_TABLE("RBVD01020024", false, "Parametros requeridos incompletos para el registro de simulacion producto"),
    ERROR_CONNECTION_CRYPTO_ASO_SERVICE("RBVD01020025", false, "No se pudo realizar una conexión con el servicio Crypto ASO"),
    ERROR_CONNECTION_TIER_ASO_SERVICE("RBVD01020026", false, "No se pudo realizar una conexión con el servicio Tier ASO"),
    INVALID_RIMAC_QUOTATION_ID("RBVD01020027", false, "El codigo de cotizacion Rimac enviado no esta registrado"),
    INVALID_PRODUCT_TYPE_AND_MODALITY_TYPE("RBVD01020028", false, "El tipo de producto y modalidad son incorrectos"),
    QUOTATION_INSERTION_WAS_WRONG("RBVD01020029", false, "Hubo un problema en la insercion de la tabla de cotizacion"),
    QUOTATION_MOD_INSERTION_WAS_WRONG("RBVD01020030", false, "Hubo un problema en la insercion de la tabla de cotizacion-mod"),
    COULDNT_SELECT_MODALITY_RIMAC_ERROR("RBVD01020031", false, "El consumo del servicio de seleccionar plan (Rimac) no fue exitoso"),
    QUOTATION_MOD_UPDATE_WAS_WRONG("RBVD01020041", false, "Hubo un problema en la actualizacion de la tabla de cotizacion-mod"),
    ERROR_CONNECTION_GETCUSTOMER_ASO("RBVD01020042", false, "Error al conectar con el servicio ASO - GETCUSTOMER");

    private final String adviceCode;
    private final boolean rollback;
    private final String message;

    public String getAdviceCode() {
        return this.adviceCode;
    }

    public boolean isRollback() {
        return this.rollback;
    }

    public String getMessage() {
        return this.message;
    }

    private RBVDErrors(String adviceCode, boolean rollback, String message) {
        this.adviceCode = adviceCode;
        this.rollback = rollback;
        this.message = message;
    }
}
