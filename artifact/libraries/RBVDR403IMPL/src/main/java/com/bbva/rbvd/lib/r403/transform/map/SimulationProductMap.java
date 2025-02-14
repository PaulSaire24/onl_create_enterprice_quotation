package com.bbva.rbvd.lib.r403.transform.map;

import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.EnterpriseQuotationDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.dto.CreateQuotationDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.QuotationResponseBO;
import com.bbva.rbvd.dto.enterpriseinsurance.utils.ConstantsUtil;
import com.bbva.rbvd.lib.r403.utils.ContansUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class SimulationProductMap {
    private SimulationProductMap(){}

    public static Map<String, Object> createArgumentsForSaveSimulationProduct(BigDecimal nextId,
                                                                              EnterpriseQuotationDTO response, BigDecimal productId) {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put(ConstantsUtil.InsuranceSimulation.FIELD_INSURANCE_SIMULATION_ID, nextId);
        arguments.put(ConstantsUtil.QuotationModMap.INSURANCE_PRODUCT_ID, productId);
        arguments.put(ConstantsUtil.QuotationModMap.FIELD_CAMPAIGN_FACTOR_TYPE, null);
        arguments.put(ConstantsUtil.QuotationModMap.FIELD_CAMPAIGN_OFFER_1_AMOUNT, 0);
        arguments.put(ConstantsUtil.QuotationModMap.FIELD_CAMPAIGN_FACTOR_PER, 0);
        arguments.put(ConstantsUtil.InsurancePrdModality.FIELD_SALE_CHANNEL_ID, response.getSaleChannelId());
        arguments.put(ConstantsUtil.QuotationMap.SOURCE_BRANCH_ID, response.getSourceBranchCode());
        arguments.put(ConstantsUtil.QuotationMap.CREATION_USER_ID, response.getCreationUser());
        arguments.put(ConstantsUtil.QuotationMap.USER_AUDIT_ID, response.getUserAudit());

        return arguments;
    }

}
