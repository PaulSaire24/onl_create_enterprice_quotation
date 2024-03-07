package com.bbva.rbvd.lib.r403.transform.bean;

import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.DescriptionDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.ParticipantDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.ProductDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.rimac.PlanBO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.dto.CreateQuotationDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.InsuranceEnterpriseResponseBO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.QuotationResponseBO;
import com.bbva.rbvd.lib.r403.service.dao.PlanDAO;
import com.bbva.rbvd.lib.r403.service.impl.ConsumerInternalService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class QuotationRimac {
    private QuotationRimac() {
    }

    public static CreateQuotationDTO mapInQuotationResponse(CreateQuotationDTO input, InsuranceEnterpriseResponseBO payload, String branchCode, BigDecimal nextId) {
        CreateQuotationDTO response = input;
        QuotationResponseBO responseRimac= payload.getPayload();

        PlanDAO planDAO = new PlanDAO();
        ConsumerInternalService consumerInternalService = new ConsumerInternalService();

        DescriptionDTO businessAgentResponse = Objects.nonNull(input.getBusinessAgent()) ? consumerInternalService.getBusinessAgent(input.getBusinessAgent().getId()) : null;
        List<ParticipantDTO> participantResponse = listParticipants(input);
        ProductDTO productResponse = consumerInternalService.getProduct(input.getProduct().getId(), responseRimac);
        response.setParticipants(participantResponse);
        response.setBusinessAgent(businessAgentResponse);
        response.setProduct(productResponse);
        response.getProduct().setPlans(Objects.nonNull(response.getProduct()) && Objects.nonNull(responseRimac.getCotizaciones())  ? planDAO.getPlanInfo(listPlans(responseRimac)) : null);
        response.setId(generateQuotationId(branchCode, nextId, input));

        return response;
    }
    public static List<PlanBO> listPlans(QuotationResponseBO responseRimac) {
        List<PlanBO> planBOList = new ArrayList<>();
        Integer i ;
        for (i = 0; i < responseRimac.getCotizaciones().size(); i++) {
                planBOList.add(responseRimac.getCotizaciones().get(i).getPlan());
        }
        return planBOList;
    }

    public static List<ParticipantDTO> listParticipants(CreateQuotationDTO input){
        List<ParticipantDTO> listaParticipantes = new ArrayList<>();

        List<ParticipantDTO> inputParticipants = Objects.nonNull(input) ? input.getParticipants() : null;

        if (Objects.nonNull(inputParticipants)) {
            listaParticipantes.addAll(inputParticipants);
        }

        return listaParticipantes;
    }
    public static String generateQuotationId(String branchCode,BigDecimal nextId,CreateQuotationDTO input){
      // Pasar solo el input, Queda por ahora asì, igual es configurable.
        //0814 842 IdSimulacion 01. en el bussiness

        Objects.requireNonNull(branchCode, "branchCode no puede ser nulo");
        Objects.requireNonNull(nextId, "nextId no puede ser nulo");
        Objects.requireNonNull(input, "input no puede ser nulo");

        String simulationId = nextId.toString();
        String product = input.getProduct().getId();

        String quotationId = branchCode.concat("0000").concat(simulationId).concat(product);
        return quotationId;
    }
}
