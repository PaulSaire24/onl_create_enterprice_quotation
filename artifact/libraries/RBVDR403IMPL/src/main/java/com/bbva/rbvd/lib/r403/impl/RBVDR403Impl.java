package com.bbva.rbvd.lib.r403.impl;


import com.bbva.apx.exception.business.BusinessException;

import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.EnterpriseQuotationDTO;

import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.ParticipantDTO;

import com.bbva.rbvd.dto.enterpriseinsurance.utils.ConstantsUtil;
import com.bbva.rbvd.lib.r403.pattern.impl.CreateQuotationStore;
import com.bbva.rbvd.lib.r403.pattern.product.CreateQuotationVidaLey;
import com.bbva.rbvd.lib.r403.pattern.impl.CreateQuotationParameter;
import com.bbva.rbvd.lib.r403.pattern.CreateQuotation;

import com.bbva.rbvd.lib.r403.utils.ContansUtils;
import com.bbva.rbvd.lib.r403.utils.RBVDErrors;
import com.bbva.rbvd.lib.r403.utils.RBVDValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;


import java.util.List;



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
			response = createQuotation.start(quotationCreate, this.applicationConfigurationService, this.externalApiConnector, this.pisdR014, this.pisdR402);
		}


		return response;
	}

	public void validInput(EnterpriseQuotationDTO input) throws BusinessException {

		List<ParticipantDTO> participant = input.getParticipants();
		if (input.getEmployees().getMonthlyPayrollAmount().getAmount() <= 0.0) {
			throw RBVDValidation.build(RBVDErrors.WRONG_INPUT_AMOUNT);

		} else if (input.getEmployees().getEmployeesNumber() <= 0L) {
			throw RBVDValidation.build(RBVDErrors.WRONG_INPUT_EMPLOYEE);

		}
		if (!CollectionUtils.isEmpty(participant)) {
			participant.stream()
					.filter(participants -> !ContansUtils.StringsUtils.RUC.equals(participants.getIdentityDocument().getDocumentType().getId()))
					.forEach(participants -> {
						throw RBVDValidation.build(RBVDErrors.WRONG_INPUT_DOCUMENT_ID);
					});
		}
	}
}
