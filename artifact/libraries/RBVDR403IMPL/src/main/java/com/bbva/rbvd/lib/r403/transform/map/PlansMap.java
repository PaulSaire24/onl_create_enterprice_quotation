package com.bbva.rbvd.lib.r403.transform.map;

import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.dto.CreateQuotationDTO;
import com.bbva.rbvd.lib.r403.utils.ContansUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PlansMap {
    private PlansMap() {
    }
    public static Map<String, Object> createArgumentsForGetPlansId(CreateQuotationDTO input,String channelCode) {
        Map<String, Object> arguments = new HashMap<>();

        if (Objects.nonNull(input) && Objects.nonNull(input.getProduct()) && Objects.nonNull(input.getProduct().getId())) {
            arguments.put(ContansUtils.Mapper.FIELD_INSURANCE_PRODUCT_ID, input.getProduct().getId());
            arguments.put(ContansUtils.Mapper.FIELD_SALE_CHANNEL_ID, channelCode);

        } else {

            arguments.put(ContansUtils.Mapper.FIELD_INSURANCE_PRODUCT_TYPE, null);
            arguments.put(ContansUtils.Mapper.FIELD_SALE_CHANNEL_ID, null);
        }
        return arguments;
    }
}
