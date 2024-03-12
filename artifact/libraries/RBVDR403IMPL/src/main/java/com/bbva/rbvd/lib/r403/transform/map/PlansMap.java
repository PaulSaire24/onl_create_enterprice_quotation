package com.bbva.rbvd.lib.r403.transform.map;


import com.bbva.rbvd.lib.r403.utils.ContansUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class PlansMap {
    private PlansMap() {
    }

    public static Map<String, Object> createArgumentsForGetPlansId(String channelCode, BigDecimal productId) {
        Map<String, Object> arguments = new HashMap<>();

        arguments.put(ContansUtils.Mapper.FIELD_INSURANCE_PRODUCT_ID, productId);
        arguments.put(ContansUtils.Mapper.FIELD_SALE_CHANNEL_ID, channelCode);

        return arguments;
    }
}
