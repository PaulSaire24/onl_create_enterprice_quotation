package com.bbva.rbvd.lib.r403.transform.map;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.DescriptionDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.EnterpriseQuotationDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.ParticipantDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.InsuranceEnterpriseResponseBO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.QuotationBO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.QuotationResponseBO;
import com.bbva.rbvd.dto.enterpriseinsurance.utils.ConstantsUtil;
import com.bbva.rbvd.lib.r403.utils.ContansUtils;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class QuotationMap {

    public static Map<String, Object> createArgumentsForSaveQuotation(BigDecimal nextId, EnterpriseQuotationDTO responseDTO,
                                  InsuranceEnterpriseResponseBO payload, ApplicationConfigurationService applicationConfigurationService){

        Map<String, Object> arguments = new HashMap<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        QuotationResponseBO responseRimac = payload.getPayload();
        QuotationBO firstQuotation = responseRimac.getCotizaciones().get(0);

        SimpleDateFormat dateFormatEnd = new SimpleDateFormat("dd/MM/yyyy");
        LocalDate fechaFin = LocalDate.parse(firstQuotation.getFechaFinVigencia());
        LocalDateTime fechaFinDate = fechaFin.atStartOfDay();

        // Convertir LocalDateTime a Date
        Date date = Date.from(fechaFinDate.atZone(ZoneId.systemDefault()).toInstant());
        arguments.put(ConstantsUtil.QuotationMap.INSURANCE_COMPANY_QUOTA_ID,firstQuotation.getCotizacion());
        arguments.put(ConstantsUtil.QuotationMap.POLICY_QUOTA_END_VALIDITY_DATE, dateFormatEnd.format(date));
        arguments.put(ConstantsUtil.QuotationMap.INSURANCE_SIMULATION_ID, nextId);

        if (!CollectionUtils.isEmpty(responseDTO.getParticipants()) && checkParticipant(responseDTO.getParticipants()).equals(true)) {

            arguments.put(ConstantsUtil.QuotationMap.CUSTOMER_ID, responseDTO.getParticipants().get(0).getId());
            arguments.put(ConstantsUtil.QuotationMap.PERSONAL_DOC_TYPE, applicationConfigurationService.getProperty(responseDTO.getParticipants().get(0).getIdentityDocument().getDocumentType().getId()));
            arguments.put(ConstantsUtil.QuotationMap.PARTICIPANT_PERSONAL_ID, responseDTO.getParticipants().get(0).getIdentityDocument().getDocumentNumber());
        } else {
            arguments.put(ConstantsUtil.QuotationMap.CUSTOMER_ID, null);
            arguments.put(ConstantsUtil.QuotationMap.PERSONAL_DOC_TYPE, null);
            arguments.put(ConstantsUtil.QuotationMap.PARTICIPANT_PERSONAL_ID, null);
        }

        arguments.put(ConstantsUtil.QuotationMap.POLICY_QUOTA_INTERNAL_ID, responseDTO.getId());
        arguments.put(ConstantsUtil.QuotationMap.QUOTE_DATE, dateFormat.format(new Date()));
        arguments.put(ContansUtils.Mapper.FIELD_POLICY_QUOTA_STATUS_TYPE, "COT");
        arguments.put(ConstantsUtil.QuotationMap.LAST_CHANGE_BRANCH_ID, responseDTO.getLastChangeBranchId());
        arguments.put(ConstantsUtil.QuotationMap.SOURCE_BRANCH_ID, responseDTO.getSourceBranchCode());
        arguments.put(ConstantsUtil.QuotationMap.CREATION_USER_ID, responseDTO.getCreationUser());
        arguments.put(ConstantsUtil.QuotationMap.USER_AUDIT_ID, responseDTO.getUserAudit());
        arguments.put(ContansUtils.Mapper.FIELD_INSURED_CUSTOMER_NAME, null);
        arguments.put(ContansUtils.Mapper.FIELD_CLIENT_LAST_NAME, null);
        arguments.put(ContansUtils.Mapper.FIELD_RFQ_INTERNAL_ID, responseDTO.getQuotationReference());

        return arguments;
    }

    private static Boolean checkParticipant(List<ParticipantDTO> participant){
        return participant.stream()
                .anyMatch(particip -> particip.getParticipantType().getId().equals("HOLDER"));
    }
    }
