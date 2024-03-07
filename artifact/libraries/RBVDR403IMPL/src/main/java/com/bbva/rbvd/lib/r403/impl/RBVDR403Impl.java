package com.bbva.rbvd.lib.r403.impl;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.dto.CreateQuotationDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.InsuranceEnterpriseInputBO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.InsuranceEnterpriseResponseBO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.QuotationInputBO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.QuotationResponseBO;
import com.bbva.rbvd.lib.r403.service.dao.IInsuranceSimulationDAO;
import com.bbva.rbvd.lib.r403.service.dao.ISimulationDAO;
import com.bbva.rbvd.lib.r403.service.dao.ISimulationProductDAO;
import com.bbva.rbvd.lib.r403.service.dao.impl.InsuranceSimulationDAOImpl;
import com.bbva.rbvd.lib.r403.service.dao.impl.SimulationDAOImpl;
import com.bbva.rbvd.lib.r403.service.dao.impl.SimulationProductDAOImpl;
import com.bbva.rbvd.lib.r403.service.impl.ConsumerExternalService;
import com.bbva.rbvd.lib.r403.transform.bean.QuotationBean;
import com.bbva.rbvd.lib.r403.transform.map.SimulationMap;
import com.bbva.rbvd.lib.r403.transform.map.SimulationProductMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

import static com.bbva.rbvd.lib.r403.transform.bean.QuotationRimac.mapInQuotationResponse;

/**
 * The RBVDR403Impl class...
 */
public class RBVDR403Impl extends RBVDR403Abstract {
	private static final Logger LOGGER = LoggerFactory.getLogger(RBVDR403Impl.class);


	/**
	 * The execute method...
	 */
	@Override
	public CreateQuotationDTO executeCreateQuotation(CreateQuotationDTO quotationCreate,String channelCode, String userAudit,String creationUser,String branchCode,String traceId) {
		CreateQuotationDTO response;
		LOGGER.info("*****executeCreateQuotation - participant : {}***",quotationCreate.getParticipants());

		InsuranceEnterpriseInputBO rimacInput = QuotationBean.createQuotationDAO(quotationCreate);
	   ConsumerExternalService consumerExternalService = new ConsumerExternalService();
		InsuranceEnterpriseResponseBO responseRimac = consumerExternalService.callRimacService(rimacInput,traceId,this.pisdR014,this.externalApiConnector);
		BigDecimal nextId = this.getInsuranceSimulationId();
		response = mapInQuotationResponse(quotationCreate,responseRimac,branchCode,nextId);
		LOGGER.info("*****executeCreateQuotation a- participant response: {}***",response);

		Map<String, Object> argumentsForSaveSimulation = SimulationMap.createArgumentsForSaveSimulation(nextId,response, quotationCreate, responseRimac, creationUser, userAudit, branchCode,this.applicationConfigurationService);
			LOGGER.info("*****executeCreateQuotation - participant argumentsForSaveSimulation: {}***", argumentsForSaveSimulation);

		Map<String, Object> argumentsForSaveSimulationProd = SimulationProductMap.createArgumentsForSaveSimulationProduct(nextId,quotationCreate,creationUser,userAudit);
		LOGGER.info("*****executeCreateQuotation - participant argumentsForSaveSimulationProd: {}***",argumentsForSaveSimulationProd);
		ISimulationProductDAO iSimulationProductDAO = new SimulationProductDAOImpl(this.pisdR402);
		ISimulationDAO iSimulationDAO = new SimulationDAOImpl(this.pisdR402);
        iSimulationDAO.insertSimulation(argumentsForSaveSimulation);
		iSimulationProductDAO.insertSimulationProduct(argumentsForSaveSimulationProd);

		return response;
	}

	public BigDecimal getInsuranceSimulationId(){
		LOGGER.info("***** executeCreateQuotation - getInsuranceSimulationId START *****");

		IInsuranceSimulationDAO insuranceSimulationDao= new InsuranceSimulationDAOImpl(pisdR402);
		BigDecimal simulationNextValue = insuranceSimulationDao.getSimulationNextVal();

		LOGGER.info("***** executeCreateQuotation - getInsuranceSimulationId | simulationNextValue: {} *****",simulationNextValue);
		return simulationNextValue;
	}

}
