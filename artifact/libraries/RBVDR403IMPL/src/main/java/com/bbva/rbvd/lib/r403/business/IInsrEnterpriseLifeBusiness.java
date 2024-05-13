package com.bbva.rbvd.lib.r403.business;

import com.bbva.rbvd.lib.r403.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r403.transfer.PayloadStore;

public interface IInsrEnterpriseLifeBusiness {
    PayloadStore doEnterpriseLife(PayloadConfig payloadConfig);
}
