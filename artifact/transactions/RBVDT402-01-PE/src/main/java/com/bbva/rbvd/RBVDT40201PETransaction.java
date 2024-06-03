package com.bbva.rbvd;


import com.bbva.apx.exception.business.BusinessException;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.EnterpriseQuotationDTO;
import com.bbva.rbvd.lib.r403.RBVDR403;
import com.bbva.elara.domain.transaction.RequestHeaderParamsName;
import com.bbva.elara.domain.transaction.Severity;
import com.bbva.elara.domain.transaction.response.HttpResponseCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

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

		RBVDR403 rbvdr403= this.getServiceLibrary(RBVDR403.class);

		LOGGER.info("RBVDT40201PETransaction - execute() | Start");
		LOGGER.info("Header traceId: {}", this.getRequestHeader().getHeaderParameter(RequestHeaderParamsName.REQUESTID));
		String channelCode = (String) this.getRequestHeader().getHeaderParameter(RequestHeaderParamsName.CHANNELCODE);
		String userAudit = (String) this.getRequestHeader().getHeaderParameter(RequestHeaderParamsName.USERCODE);
		String creationUser = (String) this.getRequestHeader().getHeaderParameter(RequestHeaderParamsName.USERCODE);
		String traceId = (String) this.getRequestHeader().getHeaderParameter(RequestHeaderParamsName.REQUESTID);
		String branchCode = String.valueOf(this.getContext().getTransactionRequest().getHeader().getHeaderParameter(RequestHeaderParamsName.BRANCHCODE));

		EnterpriseQuotationDTO quotationCreate = new EnterpriseQuotationDTO();
		quotationCreate.setQuotationReference(this.getQuotationreference());

		quotationCreate.setEmployees(this.getEmployees());
		quotationCreate.setInsuredAmount(this.getInsuredamount());
		quotationCreate.setProduct(this.getProduct());
		quotationCreate.setBusinessAgent(this.getBusinessagent());
		quotationCreate.setParticipants(this.getParticipants());
		quotationCreate.setContactDetails(this.getContactdetails());
		quotationCreate.setSaleChannelId(channelCode);
		quotationCreate.setUserAudit(userAudit);
		quotationCreate.setCreationUser(creationUser);
		quotationCreate.setTraceId(traceId);
		quotationCreate.setSourceBranchCode(branchCode);
		quotationCreate.setLastChangeBranchId(branchCode);
		quotationCreate.setPaymentMethod(this.getPaymentmethod());
		quotationCreate.setBank(this.getBank());

		try {
			EnterpriseQuotationDTO response = rbvdr403.executeCreateQuotation(quotationCreate);
			if (nonNull(response)) {
				LOGGER.info("RBVDT40201PETransaction - Response : {}", response);

				this.setProduct(response.getProduct());
				this.setParticipants(response.getParticipants());
				this.setEmployees(response.getEmployees());
				this.setStatus(response.getStatus());
				this.setId(response.getId());
				this.setBusinessagent(response.getBusinessAgent());
				this.setContactdetails(response.getContactDetails());
				this.setValidityperiod(response.getValidityPeriod());
				this.setQuotationreference(response.getQuotationReference());
				this.setQuotationdate(Date.from(response.getQuotationDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
				this.setPaymentmethod(response.getPaymentMethod());
				this.setInsuredamount(response.getInsuredAmount());
				this.setBank(response.getBank());
				this.setTotalamount(response.getTotalAmount());
				this.setTotalamountwithouttax(response.getTotalAmountWithoutTax());

				this.setHttpResponseCode(HttpResponseCode.HTTP_CODE_200, Severity.OK);
			} else {
				this.setSeverity(Severity.ENR);
			}

		}
		catch (BusinessException be) {
			this.addAdvice(be.getAdviceCode());
			this.setSeverity(Severity.ENR);

		}
	}

}
