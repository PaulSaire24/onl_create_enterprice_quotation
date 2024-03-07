package com.bbva.rbvd;

import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.dto.CreateQuotationDTO;
import com.bbva.rbvd.lib.r403.RBVDR403;
import com.bbva.elara.domain.transaction.RequestHeaderParamsName;
import com.bbva.elara.domain.transaction.Severity;
import com.bbva.elara.domain.transaction.response.HttpResponseCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Date;

import static java.util.Objects.nonNull;

/**
 * transaction to create a enterprice quotation
 *
 */
public class RBVDT40201PETransaction extends AbstractRBVDT40201PETransaction {

	private static final Logger LOGGER = LoggerFactory.getLogger(RBVDT40201PETransaction.class);

	/**
	 * The execute method...
	 */
	@Override


	public void execute() {
		RBVDR403 rbvdR403 = this.getServiceLibrary(RBVDR403.class);
		LOGGER.info("RBVDT40201PETransaction - Start");
		LOGGER.info("Header traceId: {}", this.getRequestHeader().getHeaderParameter(RequestHeaderParamsName.REQUESTID));
		String channelCode = (String) this.getRequestHeader().getHeaderParameter(RequestHeaderParamsName.CHANNELCODE);
		String userAudit = (String) this.getRequestHeader().getHeaderParameter(RequestHeaderParamsName.USERCODE);
		String creationUser = (String) this.getRequestHeader().getHeaderParameter(RequestHeaderParamsName.USERCODE);
		String traceId = (String) this.getRequestHeader().getHeaderParameter(RequestHeaderParamsName.REQUESTID);
		String branchCode = String.valueOf(this.getContext().getTransactionRequest().getHeader().getHeaderParameter(RequestHeaderParamsName.BRANCHCODE));

		RBVDR403 rbvdr403= this.getServiceLibrary(RBVDR403.class);
		CreateQuotationDTO quotationCreate = new CreateQuotationDTO();
		quotationCreate.setQuotationReference(this.getQuotationreference());
		quotationCreate.setEmployees(this.getEmployees());
		quotationCreate.setProduct(this.getProduct());
		quotationCreate.setBusinessAgent(this.getBusinessagent());
		quotationCreate.setParticipants(this.getParticipants());
		quotationCreate.setContactDetails(this.getContactdetails());
		CreateQuotationDTO response = rbvdr403.executeCreateQuotation(quotationCreate,channelCode,userAudit,creationUser,branchCode,traceId);

		if(nonNull(response)) {
			LOGGER.info("RBVDT40201PETransaction - Response : {}",response.toString());
			LOGGER.info("RBVDT40201PETransaction - product: {}",response.getProduct());
			LOGGER.info("RBVDT40201PETransaction - quotation reference : {}",response.getQuotationReference());
			LOGGER.info("RBVDT40201PETransaction - contactdetail : {}",response.getContactDetails());
			this.setProduct(response.getProduct());
			this.setParticipants(response.getParticipants());
			this.setEmployees(response.getEmployees());
			this.setStatus(response.getStatus());
			this.setId(response.getId());
			this.setBusinessagent(response.getBusinessAgent());
			this.setContactdetails(response.getContactDetails());
			this.setValidityperiod(response.getValidityPeriod());
			this.setQuotationreference(response.getQuotationReference());
			this.setQuotationdate(new Date());
			this.setHttpResponseCode(HttpResponseCode.HTTP_CODE_200, Severity.OK);
		} else {
			this.setSeverity(Severity.ENR);
		}


	}

}
