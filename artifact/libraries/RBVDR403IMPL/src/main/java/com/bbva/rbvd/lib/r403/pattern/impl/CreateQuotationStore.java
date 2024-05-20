package com.bbva.rbvd.lib.r403.pattern.impl;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.pisd.lib.r402.PISDR402;
import com.bbva.rbvd.lib.r403.pattern.PostCreateQuotation;
import com.bbva.rbvd.lib.r403.service.dao.IEnterpriseQuotationModDAO;
import com.bbva.rbvd.lib.r403.service.dao.IQuotationDAO;
import com.bbva.rbvd.lib.r403.service.dao.IEnterpriseQuotationCompanyLifeDAO;
import com.bbva.rbvd.lib.r403.service.dao.ISimulationDAO;
import com.bbva.rbvd.lib.r403.service.dao.ISimulationProductDAO;
import com.bbva.rbvd.lib.r403.service.dao.IEnterpriseSimulationCompanyLifeDAO;
import com.bbva.rbvd.lib.r403.service.dao.impl.QuotationDAOImpl;
import com.bbva.rbvd.lib.r403.service.dao.impl.SimulationDAOImpl;
import com.bbva.rbvd.lib.r403.service.dao.impl.SimulationProductDAOImpl;
import com.bbva.rbvd.lib.r403.service.dao.impl.EnterpriseQuotationCompanyLifeDAOImpl;
import com.bbva.rbvd.lib.r403.service.dao.impl.EnterpriseSimulationCompanyLifeDAO;
import com.bbva.rbvd.lib.r403.service.dao.impl.EnterpriseQuotationModDAOImpl;
import com.bbva.rbvd.lib.r403.transfer.PayloadStore;
import com.bbva.rbvd.lib.r403.transform.map.QuoteCompanyLifeMap;
import com.bbva.rbvd.lib.r403.transform.map.SimulationCompanyLifeMap;
import com.bbva.rbvd.lib.r403.transform.map.QuotationModMap;
import com.bbva.rbvd.lib.r403.transform.map.SimulationMap;
import com.bbva.rbvd.lib.r403.transform.map.QuotationMap;
import com.bbva.rbvd.lib.r403.transform.map.SimulationProductMap;
import com.bbva.rbvd.lib.r403.transform.map.QuotationDeleteMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        LOGGER.info("***** SimulationStore - saveQuotationMod START - argument: payloadStore {} *****", payloadStore);
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
        Map<String, Object> argumentsForSaveSimulationCoLife = SimulationCompanyLifeMap.argumentsToSaveSimuCompanyLife(payloadStore.getNextSimulationId(),
                payloadStore.getOutput(),payloadStore,payloadStore.getInsuranceProductId());
        LOGGER.info("***** SimulationStore - executeCreateQuotation() - argumentsForSaveSimulationCoLife: {} ***",argumentsForSaveSimulationCoLife);
        Map<String, Object> argumentsForSaveQuotationCoLife = QuoteCompanyLifeMap.argumentsToSaveQuoteCompanyLife(payloadStore.getOutput(),
                payloadStore,payloadStore.getInsuranceProductId());
        LOGGER.info("***** SimulationStore - executeCreateQuotation() - argumentsForSaveQuotationCoLife: {} ***",argumentsForSaveQuotationCoLife);
        Map<String, Object> argumentsForSaveQuotationMod = QuotationModMap.argumentsToSaveQuotationMod(payloadStore.getOutput(),
                payloadStore,payloadStore.getInsuranceProductId());
        LOGGER.info("***** SimulationStore - executeCreateQuotation() - argumentsForSaveQuotationMod: {} ***",argumentsForSaveQuotationMod);


        ISimulationDAO iSimulationDAO = new SimulationDAOImpl(this.pisdR402);
        ISimulationProductDAO iSimulationProductDAO = new SimulationProductDAOImpl(this.pisdR402);
        IEnterpriseSimulationCompanyLifeDAO enterpriseSimulationCompanyLifeDAO = new EnterpriseSimulationCompanyLifeDAO(this.pisdR402);
        IEnterpriseQuotationCompanyLifeDAO enterpriseQuotationCompanyLifeDAO = new EnterpriseQuotationCompanyLifeDAOImpl(this.pisdR402);
        IEnterpriseQuotationModDAO enterpriseQuotationModDAO = new EnterpriseQuotationModDAOImpl(this.pisdR402);



        IQuotationDAO iQuotationDAO = new QuotationDAOImpl(this.pisdR402);
        if(payloadStore.getOutput().getQuotationReference()==null) {
            iSimulationDAO.insertSimulation(argumentsForSaveSimulation);
            iSimulationProductDAO.insertSimulationProduct(argumentsForSaveSimulationProd);
            enterpriseSimulationCompanyLifeDAO.insertLifeEnterpriseSimuCompanyLife(argumentsForSaveSimulationCoLife);
            iQuotationDAO.insertQuotation(argumentsForSaveQuotation);
        }
        else if(payloadStore.getOutput().getQuotationReference()!=null && payloadStore.getFirstPolicyQuotaInternalId()==null){
            iSimulationDAO.insertSimulation(argumentsForSaveSimulation);
            iSimulationProductDAO.insertSimulationProduct(argumentsForSaveSimulationProd);
            enterpriseSimulationCompanyLifeDAO.insertLifeEnterpriseSimuCompanyLife(argumentsForSaveSimulationCoLife);
            iQuotationDAO.insertQuotation(argumentsForSaveQuotation);
            enterpriseQuotationModDAO.insertLifeEnterpriseQuotationMod(argumentsForSaveQuotationMod);
            enterpriseQuotationCompanyLifeDAO.insertLifeEnterpriseQuoteCompanyLife(argumentsForSaveQuotationCoLife);
        }
        else {
            QuotationDeleteMap quotationDeleteMap = new QuotationDeleteMap();
            Map<String, Object> argumentsForDeleteQuotation = quotationDeleteMap.createArgumentsForDeleteQuotation(payloadStore.getFirstPolicyQuotaInternalId());
            LOGGER.info("***** SimulationStore - executeCreateQuotation() - argumentsForSaveQuotation: {} ***", argumentsForSaveQuotation);

            iSimulationDAO.insertSimulation(argumentsForSaveSimulation);
            iSimulationProductDAO.insertSimulationProduct(argumentsForSaveSimulationProd);
            enterpriseSimulationCompanyLifeDAO.insertLifeEnterpriseSimuCompanyLife(argumentsForSaveSimulationCoLife);
            enterpriseQuotationCompanyLifeDAO.executeDeleteQuoteCompanyLife(payloadStore.getFirstPolicyQuotaInternalId(),
                    payloadStore.getInsuranceProductId());
            enterpriseQuotationModDAO.executeDeleteQuotationMod(payloadStore.getFirstPolicyQuotaInternalId(),
                    payloadStore.getInsuranceProductId());
            iQuotationDAO.deleteQuotation(argumentsForDeleteQuotation);

            iQuotationDAO.insertQuotation(argumentsForSaveQuotation);
            enterpriseQuotationModDAO.insertLifeEnterpriseQuotationMod(argumentsForSaveQuotationMod);
            enterpriseQuotationCompanyLifeDAO.insertLifeEnterpriseQuoteCompanyLife(argumentsForSaveQuotationCoLife);
        }
    }

}
