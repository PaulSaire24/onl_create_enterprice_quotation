package com.bbva.rbvd.lib.r403.transform.map;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.ParticipantDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.dto.CreateQuotationDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.InsuranceEnterpriseResponseBO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.QuotationResponseBO;
import com.bbva.rbvd.lib.r403.utils.ContansUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

public class SimulationMap {
    private SimulationMap(){}

    public static Map<String, Object> createArgumentsForSaveSimulation(BigDecimal nextId, CreateQuotationDTO responseDTO, CreateQuotationDTO input, InsuranceEnterpriseResponseBO payload, String creationUser, String userAudit, String branchId, ApplicationConfigurationService applicationConfigurationService) {
        Map<String, Object> arguments = new HashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        QuotationResponseBO response = payload.getPayload();

        if (Objects.nonNull(response) && Objects.nonNull(response.getCotizaciones()) && !response.getCotizaciones().isEmpty()) {
            arguments.put(ContansUtils.Mapper.FIELD_INSURANCE_SIMULATION_ID, nextId);
            arguments.put(ContansUtils.Mapper.FIELD_INSRNC_COMPANY_SIMULATION_ID,responseDTO.getId());
            arguments.put(ContansUtils.Mapper.FIELD_CUST_SIMULATION_EXPIRED_DATE, dateFormat.format(new Date()));
        } else {
            arguments.put(ContansUtils.Mapper.FIELD_INSURANCE_SIMULATION_ID, nextId);
            arguments.put(ContansUtils.Mapper.FIELD_INSRNC_COMPANY_SIMULATION_ID, null);
            arguments.put(ContansUtils.Mapper.FIELD_CUST_SIMULATION_EXPIRED_DATE, null);
        }

        if (Objects.nonNull(input) && Objects.nonNull(input.getParticipants()) && !input.getParticipants().isEmpty()&&checkParticipant(input.getParticipants()).equals(true)) {
            arguments.put(ContansUtils.Mapper.FIELD_CUSTOMER_ID, input.getParticipants().get(0).getId());
            arguments.put(ContansUtils.Mapper.FIELD_PERSONAL_DOC_TYPE, applicationConfigurationService.getProperty(input.getParticipants().get(0).getIdentityDocument().getDocumentType().getId()));
            arguments.put(ContansUtils.Mapper.FIELD_PARTICIPANT_PERSONAL_ID, input.getParticipants().get(0).getIdentityDocument().getDocumentNumber());
        } else {
            arguments.put(ContansUtils.Mapper.FIELD_CUSTOMER_ID, null);
            arguments.put(ContansUtils.Mapper.FIELD_PERSONAL_DOC_TYPE, null);
            arguments.put(ContansUtils.Mapper.FIELD_PARTICIPANT_PERSONAL_ID, null);
        }

        arguments.put(ContansUtils.Mapper.FIELD_CUSTOMER_SIMULATION_DATE, dateFormat.format(new Date()));
        arguments.put(ContansUtils.Mapper.FIELD_BANK_FACTOR_TYPE, null);
        arguments.put(ContansUtils.Mapper.FIELD_BANK_FACTOR_AMOUNT, null);
        arguments.put(ContansUtils.Mapper.FIELD_REGISTRY_SITUATION_TYPE, "01");
        arguments.put(ContansUtils.Mapper.FIELD_BANK_FACTOR_PER, null);
        arguments.put(ContansUtils.Mapper.FIELD_SOURCE_BRANCH_ID, branchId);
        arguments.put(ContansUtils.Mapper.FIELD_CREATION_USER_ID, creationUser);
        arguments.put(ContansUtils.Mapper.FIELD_USER_AUDIT_ID, userAudit);
        arguments.put(ContansUtils.Mapper.FIELD_INSURED_CUSTOMER_NAME, null);
        arguments.put(ContansUtils.Mapper.FIELD_CLIENT_LAST_NAME, null);
        arguments.put(ContansUtils.Mapper.FIELD_CUSTOMER_SEGMENT_NAME, null);
        return arguments;
    }
    private static Boolean checkParticipant(List<ParticipantDTO> participant){
        Boolean isHolder = participant.stream()
                .anyMatch(particip -> particip.getParticipantType().equals("HOLDER"));
        return isHolder;
    }
}
