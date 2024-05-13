package com.bbva.rbvd.lib.r403.transform.list;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.PlanDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.rimac.PlanBO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.dao.InsuranceModalityDAO;

import java.util.List;
import java.util.Map;

public interface IListEnterprisePlan {
    List<PlanDTO> getPlanInfo(List<PlanBO> planBOList, ApplicationConfigurationService applicationConfigurationService,
                              List<InsuranceModalityDAO> planList);
}
