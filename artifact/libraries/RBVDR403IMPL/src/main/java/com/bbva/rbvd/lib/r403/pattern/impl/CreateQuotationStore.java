package com.bbva.rbvd.lib.r403.pattern.impl;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.pisd.dto.insurance.dao.SimulationProductDAO;
import com.bbva.pisd.lib.r402.PISDR402;
import com.bbva.rbvd.dto.enterpriseinsurance.modifyquotation.dao.InsuranceQuotationModDAO;
import com.bbva.rbvd.lib.r403.pattern.PostCreateQuotation;
import com.bbva.rbvd.lib.r403.service.dao.IQuotationDAO;
import com.bbva.rbvd.lib.r403.service.dao.ISimulationDAO;
import com.bbva.rbvd.lib.r403.service.dao.ISimulationProductDAO;
import com.bbva.rbvd.lib.r403.service.dao.impl.QuotationDAOImpl;
import com.bbva.rbvd.lib.r403.service.dao.impl.SimulationDAOImpl;
import com.bbva.rbvd.lib.r403.service.dao.impl.SimulationProductDAOImpl;
import com.bbva.rbvd.lib.r403.transfer.PayloadStore;
import com.bbva.rbvd.lib.r403.transform.bean.QuotationRimac;
import com.bbva.rbvd.lib.r403.transform.map.QuotationDeleteMap;
import com.bbva.rbvd.lib.r403.transform.map.QuotationMap;
import com.bbva.rbvd.lib.r403.transform.map.SimulationMap;
import com.bbva.rbvd.lib.r403.transform.map.SimulationProductMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

public class CreateQuotationStore implements PostCreateQuotation {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateQuotationStore.class);
    private final PISDR402 pisdR402;
    private final ApplicationConfigurationService applicationConfigurationService;

    public CreateQuotationStore(PISDR402 pisdR402, ApplicationConfigurationService applicationConfigurationService) {
        this.pisdR402 = pisdR402;
        this.applicationConfigurationService =  applicationConfigurationService;
    }
    @Override
    public void end(PayloadStore payloadStore) {
        this.saveQuotation(payloadStore);
    }
    public void saveQuotation(PayloadStore payloadStore) {
        LOGGER.info("***** SimulationStore - saveQuotationMod START - arguments: payloadStore {} *****", payloadStore);
        SimulationMap simulationMap = new SimulationMap();
        Map<String, Object> argumentsForSaveSimulation = simulationMap.createArgumentsForSaveSimulation(payloadStore.getNextSimulationId(),
                payloadStore.getOutput(), payloadStore.getRimacResponse(),this.applicationConfigurationService);
        LOGGER.info("***** SimulationStore - executeCreateQuotation() - argumentsForSaveSimulation: {} ***", argumentsForSaveSimulation);

        QuotationMap quotationMap = new QuotationMap();
        Map<String, Object> argumentsForSaveQuotation = quotationMap.createArgumentsForSaveQuotation(payloadStore.getNextSimulationId(),
                payloadStore.getOutput(), payloadStore.getRimacResponse(),this.applicationConfigurationService);
        LOGGER.info("***** SimulationStore - executeCreateQuotation() - argumentsForSaveQuotation: {} ***", argumentsForSaveQuotation);

        Map<String, Object> argumentsForSaveSimulationProd = SimulationProductMap.createArgumentsForSaveSimulationProduct(payloadStore.getNextSimulationId(),
                payloadStore.getOutput(),payloadStore.getInsuranceProductId());
        LOGGER.info("***** SimulationStore - executeCreateQuotation() - argumentsForSaveSimulationProd: {} ***",argumentsForSaveSimulationProd);

        ISimulationDAO iSimulationDAO = new SimulationDAOImpl(this.pisdR402);
        ISimulationProductDAO iSimulationProductDAO = new SimulationProductDAOImpl(this.pisdR402);
        IQuotationDAO iQuotationDAO = new QuotationDAOImpl(this.pisdR402);
        if(payloadStore.getOutput().getQuotationReference().equals(null) || payloadStore.getFirstPolicyQuotaInternalId()==null) {
            iSimulationDAO.insertSimulation(argumentsForSaveSimulation);
            iSimulationProductDAO.insertSimulationProduct(argumentsForSaveSimulationProd);
            iQuotationDAO.insertQuotation(argumentsForSaveQuotation);
        }
        else{
            QuotationDeleteMap quotationDeleteMap = new QuotationDeleteMap();
            Map<String, Object> argumentsForDeleteQuotation = quotationDeleteMap.createArgumentsForDeleteQuotation(payloadStore.getFirstPolicyQuotaInternalId());
            LOGGER.info("***** SimulationStore - executeCreateQuotation() - argumentsForSaveQuotation: {} ***", argumentsForSaveQuotation);

            iSimulationDAO.insertSimulation(argumentsForSaveSimulation);
            iSimulationProductDAO.insertSimulationProduct(argumentsForSaveSimulationProd);
            iQuotationDAO.deleteQuotation(argumentsForDeleteQuotation);
            iQuotationDAO.insertQuotation(argumentsForSaveQuotation);
        }
    }

}
