package com.bbva.rbvd.lib.r403.service.dao;

import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.ProductDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.QuotationResponseBO;
import com.bbva.rbvd.lib.r403.utils.ContansUtils;

public class ProductDAO {

    public ProductDTO getProductInfo(ProductDTO productDTO){

        productDTO.setName(ContansUtils.mockInternalData.PRODUCT_NAME);
        return productDTO;
    }
    public ProductDTO getProductInfoBO(QuotationResponseBO rimacResponse){
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName(rimacResponse.getProducto());
        return productDTO;
    }
}
