package com.bbva.rbvd.lib.r403.transform.bean;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.*;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.rimac.PlanBO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.dto.CreateQuotationDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.InsuranceEnterpriseResponseBO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.QuotationBO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.QuotationResponseBO;
import com.bbva.rbvd.lib.r403.service.dao.PlanDAO;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class QuotationRimac {

    private final ApplicationConfigurationService applicationConfigurationService;

    public QuotationRimac(ApplicationConfigurationService applicationConfigurationService) {
        this.applicationConfigurationService = applicationConfigurationService;
    }

    public EnterpriseQuotationDTO mapInQuotationResponse(EnterpriseQuotationDTO input,
                                                         InsuranceEnterpriseResponseBO payload, BigDecimal nextId) {

        QuotationResponseBO responseRimac = payload.getPayload();
        QuotationBO firstQuotation = responseRimac.getCotizaciones().get(0);
        PlanDAO planDAO = new PlanDAO();

        input.getProduct().setPlans(!CollectionUtils.isEmpty(responseRimac.getCotizaciones())
                ? planDAO.getPlanInfo(listPlans(responseRimac.getCotizaciones()),this.applicationConfigurationService) : null);
        input.setId(generateQuotationId(nextId, input));
        if(CollectionUtils.isEmpty(responseRimac.getCotizaciones())){
            input.setValidityPeriod(null);
        }
        else {
            input.setValidityPeriod(createValidityPeriodDTO(responseRimac.getCotizaciones().get(0).getFechaFinVigencia()));
        }
        input.setQuotationDate(LocalDate.now());
        input.setId(firstQuotation.getCotizacion());

        return input;
    }

    private ValidityPeriodDTO createValidityPeriodDTO(String fechaFinVigencia){
        //VALIDAR CAMPO FECHA FIN Y FECHA INICIO NO NULO
        ValidityPeriodDTO validityPeriodDTO = new ValidityPeriodDTO();
        validityPeriodDTO.setStartDate(convertLocalDateToDate(LocalDate.now()));
        validityPeriodDTO.setEndDate(convertLocalDateToDate(convertStringDateToLocalDate(fechaFinVigencia)));

        return validityPeriodDTO;
    }

    private LocalDate convertStringDateToLocalDate(String date){
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    private Date convertLocalDateToDate(LocalDate date){
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static List<PlanBO> listPlans(List<QuotationBO> responseRimac) {
        List<PlanBO> planBOList = new ArrayList<>();

        for (QuotationBO quotationBO : responseRimac) {
            planBOList.add(quotationBO.getPlan());
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
    public static String generateQuotationId(BigDecimal nextId,EnterpriseQuotationDTO input){
        // Pasar solo el input, Queda por ahora asì, igual es configurable.
        //0814 842 IdSimulacion 01. en el bussiness

        String simulationId = nextId.toString();
        String product = input.getProduct().getId();

        return input.getSourceBranchCode().concat(product).concat(simulationId).concat("00");
    }
}
