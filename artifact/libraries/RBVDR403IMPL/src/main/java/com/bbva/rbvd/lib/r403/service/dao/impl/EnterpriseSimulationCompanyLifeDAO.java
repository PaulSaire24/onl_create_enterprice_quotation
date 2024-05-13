package com.bbva.rbvd.lib.r403.service.dao.impl;

import com.bbva.pisd.lib.r402.PISDR402;
import com.bbva.rbvd.dto.enterpriseinsurance.utils.ConstantsUtil;
import com.bbva.rbvd.dto.enterpriseinsurance.utils.RBVDErrors;
import com.bbva.rbvd.dto.enterpriseinsurance.utils.RBVDValidation;
import com.bbva.rbvd.lib.r403.service.dao.IEnterpriseSimulationCompanyLifeDAO;
import com.bbva.rbvd.lib.r403.transform.map.QuoteCompanyLifeMap;
import com.bbva.rbvd.lib.r403.transform.map.SimulationCompanyLifeMap;
import com.bbva.rbvd.lib.r403.utils.ContansUtils;

import java.math.BigDecimal;
import java.util.Map;

public class EnterpriseSimulationCompanyLifeDAO implements IEnterpriseSimulationCompanyLifeDAO {
    private final PISDR402 pisdR402;

    public EnterpriseSimulationCompanyLifeDAO(PISDR402 pisdR402) {
        this.pisdR402 = pisdR402;
    }

    @Override
    public void insertLifeEnterpriseSimuCompanyLife(Map<String, Object> quotationQuoteCoLife) {
        int saveSimuCoLife = this.pisdR402.executeInsertSingleRow(ContansUtils.rimacInput.INSERT_INSRNC_SIMLT_CO_LIF,quotationQuoteCoLife);

        if(saveSimuCoLife != ConstantsUtil.NumberConstants.ONE){
            throw RBVDValidation.build(RBVDErrors.ERROR_QUOTATION_MOD_SAVING);
        }
    }

}
