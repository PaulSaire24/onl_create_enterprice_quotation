package com.bbva.rbvd.lib.r403.impl;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.dto.CreateQuotationDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.InsuranceEnterpriseInputBO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.InsuranceEnterpriseResponseBO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.QuotationInputBO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.QuotationResponseBO;
import com.bbva.rbvd.lib.r403.service.dao.*;
import com.bbva.rbvd.lib.r403.service.dao.impl.*;
import com.bbva.rbvd.lib.r403.service.impl.ConsumerExternalService;
import com.bbva.rbvd.lib.r403.transform.bean.QuotationBean;
import com.bbva.rbvd.lib.r403.transform.map.*;
import com.bbva.rbvd.lib.r403.utils.ContansUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
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
		Map<String, Object> argumentsForGetPlansId = PlansMap.createArgumentsForGetPlansId(quotationCreate,channelCode);
		LOGGER.info("*****executeCreateQuotation - participant argumentsForGetPlansId: {}***", argumentsForGetPlansId);
		IInsurancePlanDAO iInsurancePlanDAO = new InsurancePlanDAO(this.pisdR402);
		Map<String, Object> planList = iInsurancePlanDAO.getPlansId(argumentsForGetPlansId);
		LOGGER.info("*****executeCreateQuotation - Lista de Planes: {}***", planList);
		InsuranceEnterpriseInputBO rimacInput = QuotationBean.createQuotationDAO(quotationCreate,planList);
	   ConsumerExternalService consumerExternalService = new ConsumerExternalService();
		InsuranceEnterpriseResponseBO responseRimac = consumerExternalService.callRimacService(rimacInput,traceId,this.pisdR014,this.externalApiConnector);
		BigDecimal nextId = this.getInsuranceSimulationId();
		response = mapInQuotationResponse(quotationCreate,responseRimac,branchCode,nextId);
		LOGGER.info("*****executeCreateQuotation a- participant response: {}***",response);
		Map<String, Object> argumentsForGetProductId = ProductMap.createArgumentsForGetProductId(quotationCreate);
		LOGGER.info("*****executeCreateQuotation - participant argumentsForGetProductId: {}***", argumentsForGetProductId);
		Object product = this.getProductId(argumentsForGetProductId);
		Map<String, Object> productMap = (Map<String, Object>) product;

		LOGGER.info("*****executeCreateQuotation -  product from BD: {}***", product);
		Map<String, Object> argumentsForSaveSimulation = SimulationMap.createArgumentsForSaveSimulation(nextId,response, quotationCreate, responseRimac, creationUser, userAudit, branchCode,this.applicationConfigurationService);
		LOGGER.info("*****executeCreateQuotation - participant argumentsForSaveSimulation: {}***", argumentsForSaveSimulation);
		Map<String, Object> argumentsForSaveQuotation = QuotationMap.createArgumentsForSaveQuotation(nextId,response, quotationCreate, responseRimac, creationUser, userAudit, branchCode,this.applicationConfigurationService);

		LOGGER.info("*****executeCreateQuotation - participant argumentsForSaveQuotation: {}***", argumentsForSaveQuotation);
		Map<String, Object> argumentsForSaveSimulationProd = SimulationProductMap.createArgumentsForSaveSimulationProduct(nextId,quotationCreate,creationUser,userAudit,getInsurancePruductId(productMap));
		LOGGER.info("*****executeCreateQuotation - participant argumentsForSaveSimulationProd: {}***",argumentsForSaveSimulationProd);
		ISimulationProductDAO iSimulationProductDAO = new SimulationProductDAOImpl(this.pisdR402);
		ISimulationDAO iSimulationDAO = new SimulationDAOImpl(this.pisdR402);
		IQuotationDAO iQuotationDAO = new QuotationDAOImpl(this.pisdR402);
        iSimulationDAO.insertSimulation(argumentsForSaveSimulation);
		iSimulationProductDAO.insertSimulationProduct(argumentsForSaveSimulationProd);
		iQuotationDAO.insertQuotation(argumentsForSaveQuotation);
		return response;
	}

	public Object getProductId(Map<String, Object> argumentsForGetProductId){
		LOGGER.info("***** executeCreateQuotation - getInsuranceSimulationId START *****");

		IInsuranceProductDAO insuranceProductDAO= new InsuranceProductDAO(pisdR401);
		Object productId = insuranceProductDAO.getInsuranceProductId(argumentsForGetProductId);

		LOGGER.info("***** executeCreateQuotation - getInsuranceSimulationId | simulationNextValue: {} *****",productId);
		return productId;
	}

	public BigDecimal getInsuranceSimulationId(){
		LOGGER.info("***** executeCreateQuotation - getInsuranceSimulationId START *****");

		IInsuranceSimulationDAO insuranceSimulationDao= new InsuranceSimulationDAOImpl(pisdR402);
		BigDecimal simulationNextValue = insuranceSimulationDao.getSimulationNextVal();

		LOGGER.info("***** executeCreateQuotation - getInsuranceSimulationId | simulationNextValue: {} *****",simulationNextValue);
		return simulationNextValue;
	}
	public String getInsurancePruductId(Map<String, Object> productMap){
		String productId = (String) productMap.get(ContansUtils.Mapper.FIELD_INSURANCE_PRODUCT_TYPE);
		return productId;
	}
}
