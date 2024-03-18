package com.bbva.rbvd.lib.r403.service.dao.impl;

import com.bbva.pisd.lib.r402.PISDR402;
import com.bbva.rbvd.dto.enterpriseinsurance.utils.ConstantsUtil;
import com.bbva.rbvd.lib.r403.service.dao.ISimulationProductDAO;
import com.bbva.rbvd.lib.r403.utils.ContansUtils;
import com.bbva.rbvd.lib.r403.utils.RBVDErrors;
import com.bbva.rbvd.lib.r403.utils.RBVDValidation;

import java.util.Map;

public class SimulationProductDAOImpl implements ISimulationProductDAO {
    private final PISDR402 pisdR402;

    public SimulationProductDAOImpl(PISDR402 pisdR402) {
        this.pisdR402 = pisdR402;
    }
    @Override
    public void insertSimulationProduct(Map<String,Object> argument) {

        int idInsSimulation = this.pisdR402.executeInsertSingleRow(ConstantsUtil.QueriesName.INSERT_INSRNC_SIMLT_PRD_ENTERPRICE,argument);
        if(idInsSimulation != 1) {
            throw RBVDValidation.build(RBVDErrors.INSERTION_ERROR_IN_SIMULATION_PRD_TABLE);
        }
    }
}
