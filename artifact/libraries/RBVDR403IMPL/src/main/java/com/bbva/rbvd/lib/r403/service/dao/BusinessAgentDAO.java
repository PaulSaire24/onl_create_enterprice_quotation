package com.bbva.rbvd.lib.r403.service.dao;

import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.DescriptionDTO;
import com.bbva.rbvd.lib.r403.utils.ContansUtils;

public class BusinessAgentDAO {
    public DescriptionDTO MockBDResponseBA(){
        DescriptionDTO businessAgentResponse = new DescriptionDTO();
        businessAgentResponse.setName(ContansUtils.mockInternalData.BUSINESS_AGENT_NAME);
        return businessAgentResponse;
    }
}
