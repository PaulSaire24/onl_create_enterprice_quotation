package com.bbva.rbvd.lib.r403.pattern;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.elara.utility.api.connector.APIConnector;
import com.bbva.pisd.lib.r014.PISDR014;
import com.bbva.pisd.lib.r402.PISDR402;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.EnterpriseQuotationDTO;

public interface CreateQuotation {
    EnterpriseQuotationDTO start(EnterpriseQuotationDTO input, ApplicationConfigurationService applicationConfigurationService, APIConnector externalApiConnector, PISDR014 pisdR014, PISDR402 pisdR402);

}
