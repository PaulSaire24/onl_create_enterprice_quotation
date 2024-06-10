package com.bbva.rbvd.lib.r403.transform.list.impl;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.AmountDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.DescriptionDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.CoverageDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.InstallmentPlansDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.rimac.PlanBO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.rimac.CoverageBO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.rimac.FinancingBO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.rimac.ParticularDataBO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.rimac.AssistanceBO;
import com.bbva.rbvd.lib.r403.utils.ContansUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Collections;
import java.util.stream.Collectors;

public class ListEnterprisePlan  {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListEnterprisePlan.class);
    private final ApplicationConfigurationService applicationConfigurationService;

    public ListEnterprisePlan(ApplicationConfigurationService applicationConfigurationService) {
        this.applicationConfigurationService = applicationConfigurationService;
    }
    public static AmountDTO mapTotalInstallmentPlans(PlanBO rimacPlan, ApplicationConfigurationService applicationConfigurationService) {
        if (Objects.isNull(rimacPlan.getPrimaBruta()) || Objects.isNull(rimacPlan.getMoneda())) {
            return null;
        }
        if(rimacPlan.getMoneda().equals(ContansUtils.StringsUtils.AMOUNT_PEN)||
                rimacPlan.getMoneda().equals(ContansUtils.StringsUtils.AMOUNT_SOL)){
            rimacPlan.setMoneda(ContansUtils.StringsUtils.AMOUNT_PEN);
        }
        else{
            rimacPlan.setMoneda(ContansUtils.StringsUtils.AMOUNT_USD);
        }
        FinancingBO primerValor = new FinancingBO();
        Iterator<FinancingBO> iterador = rimacPlan.getFinanciamientos().iterator();
        if (iterador.hasNext()) {
             primerValor = iterador.next();
        }

        AmountDTO totalInstallmentPlan = new AmountDTO();
        totalInstallmentPlan.setAmount(rimacPlan.getPrimaBruta().doubleValue());
        totalInstallmentPlan.setCurrency(rimacPlan.getMoneda());
        totalInstallmentPlan.setPeriod(mapPeriod(primerValor,applicationConfigurationService));

        return totalInstallmentPlan;
    }

    public static List<CoverageDTO> mapCoverages(List<ParticularDataBO> particularData,PlanBO rimacPlan, ApplicationConfigurationService applicationConfigurationService) {
        if (CollectionUtils.isEmpty(particularData)||CollectionUtils.isEmpty(rimacPlan.getCoberturas())) {
            return Collections.emptyList();
        }
        List<String> sumaAsegurada = particularData.stream()
                .filter(dato -> dato.getEtiqueta().equals(ContansUtils.StringsUtils.SUMA_ASEGURADA))
                .map(ParticularDataBO::getValor)
                .collect(Collectors.toList());
        String firstSumaAsegurada = null;
        Iterator<String> iterador = sumaAsegurada.iterator();
        if (iterador.hasNext()) {
            firstSumaAsegurada = iterador.next();
        }

            Double sumAsegurada = Double.valueOf(firstSumaAsegurada);

        return rimacPlan.getCoberturas().stream()
                .map(coverageBO -> {

                    CoverageDTO coverageDTO = new CoverageDTO();
                    coverageDTO.setCoverageType(mapCoverageType(coverageBO,applicationConfigurationService));
                    coverageDTO.setId(coverageBO.getCobertura().toString());
                    coverageDTO.setDescription(coverageBO.getNumeroSueldos()+ ContansUtils.rimacInput.REMUNERACIONES);
                    coverageDTO.setName(coverageBO.getObservacionCobertura());
                    coverageDTO.setInsuredAmount(mapInsuredAmount(sumAsegurada,rimacPlan));
                    return coverageDTO;
                })
                .collect(Collectors.toList());
    }
    public static List<DescriptionDTO> mapBenefits(PlanBO rimacPlan) {
        if (CollectionUtils.isEmpty(rimacPlan.getAsistencias())) {
            return Collections.emptyList();
        }

        List<AssistanceBO> assistanceBOList =rimacPlan.getAsistencias();
        return assistanceBOList.stream()
                .map(assistanceBO -> {
                    DescriptionDTO benefitsDTO = new DescriptionDTO();

                    benefitsDTO.setId(assistanceBO.getAsistencia().toString());
                    benefitsDTO.setName(assistanceBO.getDescripcionAsistencia());

                    return benefitsDTO;
                })
                .collect(Collectors.toList());
    }
    public static List<InstallmentPlansDTO> mapInstallmentPlans(PlanBO rimacPlan, ApplicationConfigurationService applicationConfigurationService) {
        if (CollectionUtils.isEmpty(rimacPlan.getFinanciamientos())) {
            return Collections.emptyList();
        }
        List<FinancingBO> financingBOList = rimacPlan.getFinanciamientos();

        return financingBOList.stream()
                .map(financingBO -> {
                    InstallmentPlansDTO installmentPlansDTO = new InstallmentPlansDTO();
                    installmentPlansDTO.setPaymentsTotalNumber(financingBO.getNumeroCuotas());
                    installmentPlansDTO.setPaymentAmount(mapPaymentAmount(financingBO));
                    installmentPlansDTO.setPeriod(mapPeriod(financingBO,applicationConfigurationService));
                    return installmentPlansDTO;
                })
                .collect(Collectors.toList());
    }
    private static AmountDTO mapPaymentAmount(FinancingBO rimacPlan) {
        if (Objects.isNull(rimacPlan)) {
            return null;
        }

        AmountDTO paymentAmount = new AmountDTO();
        paymentAmount.setAmount(rimacPlan.getCuotasFinanciamiento().get(0).getMonto().doubleValue());
        if(rimacPlan.getCuotasFinanciamiento().get(0).getMoneda().equals(ContansUtils.StringsUtils.AMOUNT_PEN)||
                rimacPlan.getCuotasFinanciamiento().get(0).getMoneda().equals(ContansUtils.StringsUtils.AMOUNT_SOL)){
            rimacPlan.getCuotasFinanciamiento().get(0).setMoneda(ContansUtils.StringsUtils.AMOUNT_PEN);
        }
        else{
            rimacPlan.getCuotasFinanciamiento().get(0).setMoneda(ContansUtils.StringsUtils.AMOUNT_USD);
        }
        paymentAmount.setCurrency(rimacPlan.getCuotasFinanciamiento().get(0).getMoneda());

        return paymentAmount;
    }
    private static AmountDTO mapInsuredAmount(Double sumaAsegurada, PlanBO rimacPlan) {
        AmountDTO insuredAmount = new AmountDTO();
        if (!Objects.isNull(rimacPlan)) {
        insuredAmount.setAmount(sumaAsegurada);
        if(rimacPlan.getMoneda().equals(ContansUtils.StringsUtils.AMOUNT_PEN)||
                rimacPlan.getMoneda().equals(ContansUtils.StringsUtils.AMOUNT_SOL)){
            rimacPlan.setMoneda(ContansUtils.StringsUtils.AMOUNT_PEN);
        }
        else{
            rimacPlan.setMoneda(ContansUtils.StringsUtils.AMOUNT_USD);
        }
        insuredAmount.setCurrency(rimacPlan.getMoneda());
    }
        return insuredAmount;

    }
    private static DescriptionDTO mapCoverageType(CoverageBO coverageBO, ApplicationConfigurationService applicationConfigurationService) {
        if (Objects.isNull(coverageBO.getPrincipal())) {
            return null;
        }

        LOGGER.info("***** InsrEnterpriseLifeBusinessPlanImpl - mapCoverageType  |  coverageBO response: {} *****",  coverageBO.getPrincipal());
        LOGGER.info("***** InsrEnterpriseLifeBusinessPlanImpl - mapCoverageType  |  key console: {} *****",  ContansUtils.StringsUtils.COVERAGE_TYPE+ coverageBO.getPrincipal());
        DescriptionDTO coverageType = new DescriptionDTO();
        String condition = applicationConfigurationService.getProperty(ContansUtils.StringsUtils.COVERAGE_TYPE+ coverageBO.getPrincipal());
        LOGGER.info("***** InsrEnterpriseLifeBusinessPlanImpl - mapCoverageType  |  condition: {} *****", condition);
        coverageType.setId(condition);
        coverageType.setName(condition);

        return coverageType;
    }
    private static DescriptionDTO mapPeriod(FinancingBO rimacPlan,ApplicationConfigurationService applicationConfigurationService) {
        DescriptionDTO period = new DescriptionDTO();

        if (Objects.isNull(rimacPlan.getPeriodicidad())) {
            return null;
        }
        //PONER EN EL DTO LA CLAVE
        period.setId(applicationConfigurationService.getProperty(ContansUtils.StringsUtils.PERIODICITY_ID + rimacPlan.getPeriodicidad()));
        period.setName(applicationConfigurationService.getProperty(ContansUtils.StringsUtils.PERIODICITY_NAME + rimacPlan.getPeriodicidad()));

        return period;
    }
    }
