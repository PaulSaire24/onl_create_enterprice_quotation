package com.bbva.rbvd.lib.r403.service.impl;

import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.DescriptionDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.ParticipantDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.ProductDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.QuotationResponseBO;
import com.bbva.rbvd.lib.r403.service.dao.BusinessAgentDAO;
import com.bbva.rbvd.lib.r403.service.dao.ParticipantDAO;
import com.bbva.rbvd.lib.r403.service.dao.ProductDAO;


public class ConsumerInternalService {
    public DescriptionDTO getBusinessAgent (String id){
        DescriptionDTO businessAgent;
        BusinessAgentDAO businessAgentDAO = new BusinessAgentDAO();
         businessAgent = businessAgentDAO.MockBDResponseBA();
        businessAgent.setId(id);
        return businessAgent;
    }
    public ProductDTO getProduct (String id,QuotationResponseBO rimacResponse){
        ProductDTO product ;
        ProductDAO productDAO = new ProductDAO();
        product = productDAO. getProductInfoBO(rimacResponse);
        product.setId(id);
        return product;
    }

}
