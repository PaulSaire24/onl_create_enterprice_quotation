package com.bbva.rbvd.lib.r403.transform.map;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.ParticipantDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.dto.CreateQuotationDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.InsuranceEnterpriseResponseBO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.QuotationResponseBO;
import com.bbva.rbvd.lib.r403.utils.ContansUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

public class QuotationMap {
    private QuotationMap(){}
    public static Map<String, Object> createArgumentsForSaveQuotation(BigDecimal nextId, CreateQuotationDTO responseDTO, CreateQuotationDTO input, InsuranceEnterpriseResponseBO payload, String creationUser, String userAudit, String branchId, ApplicationConfigurationService applicationConfigurationService) {
        Map<String, Object> arguments = new HashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        QuotationResponseBO response = payload.getPayload();

        if (Objects.nonNull(response) && Objects.nonNull(response.getCotizaciones()) && !response.getCotizaciones().isEmpty()) {
            arguments.put(ContansUtils.Mapper.FIELD_INSURANCE_COMPANY_QUOTA_ID,response.getCotizaciones().get(0).getCotizacion());

        } else {

            arguments.put(ContansUtils.Mapper.FIELD_INSURANCE_COMPANY_QUOTA_ID, null);
        }

        if (Objects.nonNull(input) && Objects.nonNull(input.getParticipants()) && !input.getParticipants().isEmpty()&&checkParticipant(input.getParticipants()).equals(true)) {

            arguments.put(ContansUtils.Mapper.FIELD_INSURANCE_SIMULATION_ID, nextId);
            arguments.put(ContansUtils.Mapper.FIELD_CUSTOMER_ID, input.getParticipants().get(0).getId());
            arguments.put(ContansUtils.Mapper.FIELD_PERSONAL_DOC_TYPE, applicationConfigurationService.getProperty(input.getParticipants().get(0).getIdentityDocument().getDocumentType().getId()));
            arguments.put(ContansUtils.Mapper.FIELD_PARTICIPANT_PERSONAL_ID, input.getParticipants().get(0).getIdentityDocument().getDocumentNumber());
        } else {
            arguments.put(ContansUtils.Mapper.FIELD_INSURANCE_SIMULATION_ID, nextId);
            arguments.put(ContansUtils.Mapper.FIELD_CUSTOMER_ID, null);
            arguments.put(ContansUtils.Mapper.FIELD_PERSONAL_DOC_TYPE, null);
            arguments.put(ContansUtils.Mapper.FIELD_PARTICIPANT_PERSONAL_ID, null);
        }
        arguments.put(ContansUtils.Mapper.FIELD_POLICY_QUOTA_INTERNAL_ID, responseDTO.getId());
        arguments.put(ContansUtils.Mapper.FIELD_QUOTE_DATE, dateFormat.format(new Date()));
        arguments.put(ContansUtils.Mapper.FIELD_QUOTA_HMS_DATE, dateFormat.format(new Date()));
        arguments.put(ContansUtils.Mapper.FIELD_POLICY_QUOTA_END_VALIDITY_DATE, "01");
        arguments.put(ContansUtils.Mapper.FIELD_POLICY_QUOTA_STATUS_TYPE, null);
        arguments.put(ContansUtils.Mapper.FIELD_LAST_CHANGE_BRANCH_ID, branchId);
        arguments.put(ContansUtils.Mapper.FIELD_SOURCE_BRANCH_ID, branchId);
        arguments.put(ContansUtils.Mapper.FIELD_CREATION_USER_ID, userAudit);
        arguments.put(ContansUtils.Mapper.FIELD_USER_AUDIT_ID, null);
        arguments.put(ContansUtils.Mapper.FIELD_INSURED_CUSTOMER_NAME, null);
        arguments.put(ContansUtils.Mapper.FIELD_CLIENT_LAST_NAME, null);
        if(Objects.nonNull(input) && Objects.nonNull(input.getQuotationReference())){
            arguments.put(ContansUtils.Mapper.FIELD_RFQ_INTERNAL_ID, input.getQuotationReference());
        }
        arguments.put(ContansUtils.Mapper.FIELD_RFQ_INTERNAL_ID, null);
        return arguments;
    }
    private static Boolean checkParticipant(List<ParticipantDTO> participant){
        Boolean isHolder = participant.stream()
                .anyMatch(particip -> particip.getParticipantType().getId().equals("HOLDER"));
        return isHolder;
    }
    }
