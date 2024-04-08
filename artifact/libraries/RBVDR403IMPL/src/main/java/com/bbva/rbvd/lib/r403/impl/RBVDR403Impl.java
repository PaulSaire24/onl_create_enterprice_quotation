package com.bbva.rbvd.lib.r403.impl;


import com.bbva.apx.exception.business.BusinessException;
import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.elara.utility.api.connector.APIConnector;
import com.bbva.pisd.lib.r014.PISDR014;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.EnterpriseQuotationDTO;

import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.ParticipantDTO;

import com.bbva.rbvd.dto.enterpriseinsurance.utils.ConstantsUtil;
import com.bbva.rbvd.lib.r403.pattern.impl.CreateQuotationStore;
import com.bbva.rbvd.lib.r403.pattern.product.CreateQuotationVidaLey;
import com.bbva.rbvd.lib.r403.pattern.impl.CreateQuotationParameter;
import com.bbva.rbvd.lib.r403.pattern.CreateQuotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * The RBVDR403Impl class...
 */
public class RBVDR403Impl extends RBVDR403Abstract {
	private static final Logger LOGGER = LoggerFactory.getLogger(RBVDR403Impl.class);


	/**
	 * The execute method...
	 */
	@Override
	public EnterpriseQuotationDTO executeCreateQuotation(EnterpriseQuotationDTO quotationCreate) {
		LOGGER.info("RBVDR403Impl - executeCreateQuotation() | START");
		validInput(quotationCreate);
		EnterpriseQuotationDTO response = new EnterpriseQuotationDTO();
		CreateQuotation createQuotation;
		if(ConstantsUtil.StringConstants.PRODUCT_VIDA_LEY_ID.equals(quotationCreate.getProduct().getId())){
			createQuotation = CreateQuotationVidaLey.Builder.an()
					.withPreSimulation(CreateQuotationParameter.Builder.an()
							.withPisdR401(this.pisdR401)
							.withPisdR402(this.pisdR402)
							.build())
					.withPostSimulation(new CreateQuotationStore(this.pisdR402,this.applicationConfigurationService))
					.build();
			LOGGER.info("***** RBVDR403Impl - CreateQuotationVidaLey ***** {}", createQuotation);
			response = createQuotation.start(quotationCreate, this.applicationConfigurationService, this.externalApiConnector, this.pisdR014);
		}


		return response;
	}

	public void validInput(EnterpriseQuotationDTO input) throws BusinessException {

		List<ParticipantDTO> participant = input.getParticipants();
		if (input.getEmployees().getMonthlyPayrollAmount().getAmount() <= 0.0) {
			LOGGER.info("***** RBVDR403Impl - executeCreateQuotation() - getAmount: {} ***", input.getEmployees().getMonthlyPayrollAmount().getAmount());
			throw new BusinessException("RBVD10094947", false, "ERROR EL MONTO NO PUEDE SER MENOR A CERO");

		} else if (input.getEmployees().getEmployeesNumber() <= 0L) {
			LOGGER.info("***** RBVDR403Impl - executeCreateQuotation() - getAmount: {} ***", input.getEmployees().getEmployeesNumber());
			throw new BusinessException("RBVD10094946", false, "ERROR EL NUMERO DE EMPLEADOS NO PUEDE SER MENOR A CERO");

		}
		if (!CollectionUtils.isEmpty(participant)) {
			// Validar que el tipo de documento de cada participante sea "RUC" usando lambdas
			participant.stream()
					.filter(participants -> !"RUC".equals(participants.getIdentityDocument().getDocumentType().getId()))
					.forEach(participants -> {
						throw new BusinessException("RBVD10094948", false, "ERROR EL TIPO DE DOCUMENTO SOLO PUEDE SER RUC");
					});
		}
	}
}
