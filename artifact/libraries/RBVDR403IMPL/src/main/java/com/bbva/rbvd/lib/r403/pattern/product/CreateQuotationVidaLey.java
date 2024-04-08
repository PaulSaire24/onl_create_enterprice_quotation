package com.bbva.rbvd.lib.r403.pattern.product;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.elara.utility.api.connector.APIConnector;
import com.bbva.pisd.lib.r014.PISDR014;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.EnterpriseQuotationDTO;
import com.bbva.rbvd.lib.r403.business.IInsrEnterpriseLifeBusiness;
import com.bbva.rbvd.lib.r403.business.impl.InsrEnterpriseLifeBusinessImpl;
import com.bbva.rbvd.lib.r403.pattern.PostCreateQuotation;
import com.bbva.rbvd.lib.r403.pattern.PreCreateQuotation;
import com.bbva.rbvd.lib.r403.pattern.impl.CreateQuotationDecorator;
import com.bbva.rbvd.lib.r403.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r403.transfer.PayloadStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateQuotationVidaLey extends CreateQuotationDecorator {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateQuotationVidaLey.class);
    public CreateQuotationVidaLey(PreCreateQuotation preCreateQuotation, PostCreateQuotation postCreateQuotation) {
        super(preCreateQuotation, postCreateQuotation);
    }
    @Override
   public EnterpriseQuotationDTO start(EnterpriseQuotationDTO input, ApplicationConfigurationService applicationConfigurationService, APIConnector externalApiConnector, PISDR014 pisdR014){
        LOGGER.info("***** RBVDR403Impl - CreateQuotationVidaLey - start START | input {} *****",input);
        PayloadConfig payloadConfig = this.getPreCreateQuotation().getConfig(input);
        IInsrEnterpriseLifeBusiness insrEnterpriseLifeBusiness = new InsrEnterpriseLifeBusinessImpl(applicationConfigurationService,externalApiConnector,pisdR014);
        PayloadStore payloadStore = insrEnterpriseLifeBusiness.doEnterpriseLife(payloadConfig);
        this.getPostCreateQuotation().end(payloadStore);
        LOGGER.info("***** RBVDR403Impl - CreateQuotationVidaLey - start END | PayloadStore.Output {} *****",payloadStore.getOutput());
        return payloadStore.getOutput();

    }
    public static final class Builder {
        private PreCreateQuotation preCreateQuotation;
        private PostCreateQuotation postCreateQuotation;

        private Builder() {
        }

        public static Builder an() {
            return new Builder();
        }

        public Builder withPreSimulation(PreCreateQuotation preCreateQuotation) {
            this.preCreateQuotation = preCreateQuotation;
            return this;
        }

        public Builder withPostSimulation(PostCreateQuotation postCreateQuotation) {
            this.postCreateQuotation = postCreateQuotation;
            return this;
        }

        public CreateQuotationVidaLey build() {
            return new CreateQuotationVidaLey(preCreateQuotation, postCreateQuotation);
        }
    }
}
