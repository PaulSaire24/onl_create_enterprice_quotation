package com.bbva.rbvd.lib.r403.service.dao.impl;

import com.bbva.pisd.lib.r401.PISDR401;
import com.bbva.pisd.lib.r402.PISDR402;
import com.bbva.rbvd.dto.enterpriseinsurance.utils.ConstantsUtil;
import com.bbva.rbvd.lib.r403.service.dao.IInsuranceSimulationDAO;
import com.bbva.rbvd.lib.r403.utils.ContansUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class InsuranceSimulationDAOImpl implements IInsuranceSimulationDAO {
    private final PISDR402 pisdR402;
    public InsuranceSimulationDAOImpl(PISDR402 pisdR402) {
        this.pisdR402 = pisdR402;
    }
    @Override
    public BigDecimal getSimulationNextVal() {

        Map<String, Object> responseGetInsuranceSimulationMap = this.pisdR402.executeGetASingleRow(ConstantsUtil.QueriesName.QUERY_SELECT_INSURANCE_SIMULATION_ID_ENTERPRISE,new HashMap<>());

        return (BigDecimal) responseGetInsuranceSimulationMap.get(ContansUtils.Querys.FIELD_Q_PISD_SIMULATION_ID0_NEXTVAL);
    }


}
