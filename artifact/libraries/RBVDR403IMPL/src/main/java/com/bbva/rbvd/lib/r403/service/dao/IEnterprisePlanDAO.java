package com.bbva.rbvd.lib.r403.service.dao;

import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.dao.InsuranceModalityDAO;

import java.math.BigDecimal;
import java.util.List;

public interface IEnterprisePlanDAO {
    List<InsuranceModalityDAO> getPlansId(BigDecimal productId, String Channel);

    }
