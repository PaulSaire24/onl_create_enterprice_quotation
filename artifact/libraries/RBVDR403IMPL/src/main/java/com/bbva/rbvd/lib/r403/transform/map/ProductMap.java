package com.bbva.rbvd.lib.r403.transform.map;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.dto.CreateQuotationDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.InsuranceEnterpriseResponseBO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.QuotationResponseBO;
import com.bbva.rbvd.lib.r403.utils.ContansUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProductMap {
    private ProductMap() {
    }

    public static Map<String, Object> createArgumentsForGetProductId(CreateQuotationDTO input) {
        Map<String, Object> arguments = new HashMap<>();

        if (Objects.nonNull(input) && Objects.nonNull(input.getProduct()) && Objects.nonNull(input.getProduct().getId())) {
            arguments.put(ContansUtils.Mapper.FIELD_INSURANCE_PRODUCT_TYPE, input.getProduct().getId());

        } else {

            arguments.put(ContansUtils.Mapper.FIELD_INSURANCE_PRODUCT_TYPE, null);
        }
        return arguments;
    }
}
