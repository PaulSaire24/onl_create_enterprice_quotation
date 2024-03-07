package com.bbva.rbvd.lib.r403.service.dao.impl;

import com.bbva.pisd.lib.r402.PISDR402;
import com.bbva.rbvd.lib.r403.service.dao.ISimulationDAO;
import com.bbva.rbvd.lib.r403.utils.ContansUtils;
import com.bbva.rbvd.lib.r403.utils.RBVDErrors;
import com.bbva.rbvd.lib.r403.utils.RBVDValidation;
import org.aspectj.apache.bcel.classfile.Constant;

import java.util.Map;

public class SimulationDAOImpl implements ISimulationDAO {
    private final PISDR402 pisdR402;

    public SimulationDAOImpl(PISDR402 pisdR402) {
        this.pisdR402 = pisdR402;
    }

    @Override
    public void insertSimulation(Map<String, Object> argument) {

        int idInsSimulation = this.pisdR402.executeInsertSingleRow(ContansUtils.Querys.INSERT_SIMULATE_ENTERPRICE, argument);
        if (idInsSimulation != 1) {
            throw RBVDValidation.build(RBVDErrors.INSERTION_ERROR_IN_SIMULATION_PRD_TABLE);
        }
    }
}
