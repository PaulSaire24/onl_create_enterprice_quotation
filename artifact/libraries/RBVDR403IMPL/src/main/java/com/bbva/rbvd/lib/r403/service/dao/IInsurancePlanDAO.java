package com.bbva.rbvd.lib.r403.service.dao;

import java.util.List;
import java.util.Map;

public interface IInsurancePlanDAO {
    List<Map<String, Object>> getPlansId(Map<String, Object> argumentsForGetPlans);

    }
