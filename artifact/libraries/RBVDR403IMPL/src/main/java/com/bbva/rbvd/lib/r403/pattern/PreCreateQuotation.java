package com.bbva.rbvd.lib.r403.pattern;

import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.EnterpriseQuotationDTO;
import com.bbva.rbvd.lib.r403.transfer.PayloadConfig;

public interface PreCreateQuotation {
    PayloadConfig getConfig(EnterpriseQuotationDTO input);
}
