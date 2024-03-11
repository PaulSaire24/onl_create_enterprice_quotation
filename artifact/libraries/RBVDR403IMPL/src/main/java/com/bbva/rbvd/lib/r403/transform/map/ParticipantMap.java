package com.bbva.rbvd.lib.r403.transform.map;

import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.ParticipantDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.PlanDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.rimac.PlanBO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.dto.CreateQuotationDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.QuotationResponseBO;
import com.bbva.rbvd.lib.r403.service.dao.ParticipantDAO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ParticipantMap {
    private ParticipantMap(){}
    public static List<PlanBO> mapPlan(Map<String,Object> planList) {
        List<PlanBO> planBOList =new ArrayList<>();
        PlanBO planBO = new PlanBO();
        planBO.setPlan(new Long(534272));
        planBOList.add(planBO);
        return planBOList;
    }

    }

