package com.bbva.rbvd.lib.r403.service.api;


import com.bbva.elara.utility.api.connector.APIConnector;

import com.bbva.pisd.dto.insurance.amazon.SignatureAWS;
import com.bbva.pisd.lib.r014.PISDR014;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.InsuranceEnterpriseInputBO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.InsuranceEnterpriseResponseBO;
import com.bbva.rbvd.lib.r403.impl.utils.JsonHelper;
import com.bbva.rbvd.lib.r403.impl.utils.RimacExceptionHandler;
import com.bbva.rbvd.lib.r403.utils.ContansUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientException;

import javax.ws.rs.HttpMethod;
import java.nio.charset.StandardCharsets;
public class ConsumerExternalService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerExternalService.class);

    public InsuranceEnterpriseResponseBO callRimacService (InsuranceEnterpriseInputBO payloadRimac, String traceId, PISDR014 pisdr014, APIConnector externalApiConnector){
        String requestJson = getRequestJson(payloadRimac);

        LOGGER.info("***** RBVDR403Impl - executeSimulationRimacService ***** Request body: {}", requestJson);

        String uri = ContansUtils.Uri.URI_RIMAC_CREARCOTIZACION;

        SignatureAWS signatureAWS = pisdr014.executeSignatureConstruction(requestJson , HttpMethod.POST,
                uri, null, traceId);
        LOGGER.info("***** RBVDR403Impl - executeSimulationRimacService() | uri : {} *****",uri);
        HttpEntity<String> entity = new HttpEntity<>(requestJson, createHttpHeadersAWS(signatureAWS));

        InsuranceEnterpriseResponseBO responseRimac = null;
        try {
            responseRimac = externalApiConnector.postForObject("enterprisequotation.create", entity, InsuranceEnterpriseResponseBO.class);
            LOGGER.info("***** RBVDR403Impl - executeSimulationRimacService ***** Response: {}", getRequestJson(responseRimac));
        } catch(RestClientException ex) {
            LOGGER.debug("***** RBVDR403Impl - executeSimulationRimacService ***** Exception: {}", ex.getMessage());
            RimacExceptionHandler exceptionHandler = new RimacExceptionHandler();
            exceptionHandler.handler(ex);
        }
        return responseRimac;
    }
    private String getRequestJson(Object o) {
        return JsonHelper.getInstance().serialization(o);
    }
    private HttpHeaders createHttpHeadersAWS(SignatureAWS signature) {
        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = new MediaType("application", "json", StandardCharsets.UTF_8);
        headers.setContentType(mediaType);
        headers.set("Authorization", signature.getAuthorization());
        headers.set("X-Amz-Date", signature.getxAmzDate());
        headers.set("x-api-key", signature.getxApiKey());
        headers.set("traceId", signature.getTraceId());
        return headers;
    }

}
