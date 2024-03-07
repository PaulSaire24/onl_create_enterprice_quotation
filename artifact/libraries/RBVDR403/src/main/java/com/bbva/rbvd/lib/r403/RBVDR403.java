package com.bbva.rbvd.lib.r403;

import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.dto.CreateQuotationDTO;

/**
 * The  interface RBVDR403 class...
 */
public interface RBVDR403 {

	/**
	 * The execute method...
	 */
	CreateQuotationDTO executeCreateQuotation(CreateQuotationDTO quotationCreate,String channelCode, String userAudit,String creationUser, String branchCode, String traceId);

}
