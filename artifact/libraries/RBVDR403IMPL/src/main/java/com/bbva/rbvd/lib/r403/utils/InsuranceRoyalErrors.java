package com.bbva.rbvd.lib.r403.utils;

public enum InsuranceRoyalErrors {
    ERROR_RIMAC_SERVICE("RBVD00120005", false, "Ocurrio un problema al consumir el servicio de Rimac"),
    SERVER_ERROR("RBVD00000158", false, "Ocurrio un problema en el servidor"),
    CLIENT_WITHOUT_PT_CONTRACT("RBVD00120018", false, "El cliente no cuenta con un contrato PT"),
    INSERTION_ERROR_IN_SIMULATION_TABLE("RBVD00120021", false, "Parametros requeridos incompletos para el registro de simulacion"),
    ERROR_FROM_RIMAC("RBVD00000119", false, "Error desde Rimac con la cotizacion enviada"),
    ERROR_CONNECTION_TIER_ASO_SERVICE("RBVD00120003", false, "No se pudo realizar una conexión con el servicio de Clasificación de Cliente");

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

    private InsuranceRoyalErrors(String adviceCode, boolean rollback, String message) {
        this.adviceCode = adviceCode;
        this.rollback = rollback;
        this.message = message;
    }
}
