package com.bbva.rbvd.lib.r403.impl;


import com.bbva.apx.exception.business.BusinessException;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.EnterpriseQuotationDTO;

import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.ParticipantDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.InsuranceEnterpriseInputBO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.InsuranceEnterpriseResponseBO;

import com.bbva.rbvd.lib.r403.service.dao.IInsurancePlanDAO;
import com.bbva.rbvd.lib.r403.service.dao.ISimulationDAO;
import com.bbva.rbvd.lib.r403.service.dao.ISimulationProductDAO;
import com.bbva.rbvd.lib.r403.service.dao.IQuotationDAO;
import com.bbva.rbvd.lib.r403.service.dao.IInsuranceProductDAO;
import com.bbva.rbvd.lib.r403.service.dao.IInsuranceSimulationDAO;
import com.bbva.rbvd.lib.r403.service.dao.impl.InsurancePlanDAO;
import com.bbva.rbvd.lib.r403.service.dao.impl.SimulationDAOImpl;
import com.bbva.rbvd.lib.r403.service.dao.impl.SimulationProductDAOImpl;
import com.bbva.rbvd.lib.r403.service.dao.impl.QuotationDAOImpl;
import com.bbva.rbvd.lib.r403.service.dao.impl.InsuranceProductDAO;
import com.bbva.rbvd.lib.r403.service.dao.impl.InsuranceSimulationDAOImpl;
import com.bbva.rbvd.lib.r403.service.impl.ConsumerExternalService;
import com.bbva.rbvd.lib.r403.transform.bean.QuotationBean;
import com.bbva.rbvd.lib.r403.transform.bean.QuotationRimac;
import com.bbva.rbvd.lib.r403.transform.map.*;
import com.bbva.rbvd.lib.r403.utils.ContansUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



/**
 * The RBVDR403Impl class...
 */
public class RBVDR403Impl extends RBVDR403Abstract {
	private static final Logger LOGGER = LoggerFactory.getLogger(RBVDR403Impl.class);


	/**
	 * The execute method...
	 */
	@Override
	public EnterpriseQuotationDTO executeCreateQuotation(EnterpriseQuotationDTO quotationCreate) {
		LOGGER.info("RBVDR403Impl - executeCreateQuotation() | START");
		EnterpriseQuotationDTO response;

		LOGGER.info("RBVDR403Impl - executeCreateQuotation() | arguments: {}",quotationCreate.toString());

		validInput(quotationCreate);
		Map<String, Object> argumentsForGetProductId = ProductMap.createArgumentsForGetProductId(
				quotationCreate.getProduct().getId());
		LOGGER.info("***** RBVDR403Impl - executeCreateQuotation() - argumentsForGetProductId: {} ***", argumentsForGetProductId);

		Map<String,Object> productMap = this.getProductInfo(argumentsForGetProductId);
		LOGGER.info("***** RBVDR403Impl - executeCreateQuotation() -  product from DB: {}***", productMap);

		BigDecimal insuranceProductId = getInsurancePruductId(productMap);
		String productName = (String) productMap.get(ContansUtils.Mapper.FIELD_PRODUCT_SHORT_DESC);
		quotationCreate.getProduct().setName(productName);

		Map<String, Object> argumentsForGetPlansId = PlansMap.createArgumentsForGetPlansId(
				quotationCreate.getSaleChannelId(),insuranceProductId);
		LOGGER.info("***** RBVDR403Impl - executeCreateQuotation() - argumentsForGetPlansId: {} ***", argumentsForGetPlansId);

		IInsurancePlanDAO iInsurancePlanDAO = new InsurancePlanDAO(this.pisdR402);
		List<Map<String, Object>> planList = iInsurancePlanDAO.getPlansId(argumentsForGetPlansId);
		LOGGER.info("***** RBVDR403Impl - executeCreateQuotation() - List of plans: {} ***", planList);

		LOGGER.info("***** RBVDR403Impl - executeCreateQuotation() - input.participants[] : {} ***",quotationCreate.getParticipants());


		List<Long> plansToRimac = new ArrayList<>();

		planList.forEach(mapa -> {
			mapa.entrySet().stream()
					.filter(entry -> ContansUtils.Mapper.FIELD_INSURANCE_COMPANY_MODALITY_ID.equals(entry.getKey()))
					.map(Map.Entry::getValue)
					.filter(value -> value instanceof Long || value instanceof String)
					.map(value -> value instanceof Long ? (Long) value : Long.parseLong((String) value))
					.forEach(plansToRimac::add);
		});

		InsuranceEnterpriseInputBO rimacInput = QuotationBean.createQuotationDAO(quotationCreate,plansToRimac,this.applicationConfigurationService);

		if(rimacInput == null){
			this.addAdviceWithDescription("RBVD0383873","Error al construir request api Crear cotizacion de Rimac");
			return null;
		}

	    ConsumerExternalService consumerExternalService = new ConsumerExternalService();
		InsuranceEnterpriseResponseBO responseRimac = consumerExternalService.callRimacService(rimacInput,
				quotationCreate.getTraceId(),this.pisdR014,this.externalApiConnector);

		BigDecimal nextId = this.getInsuranceSimulationId();

		QuotationRimac quotationRimac = new QuotationRimac(this.applicationConfigurationService);
		response = quotationRimac.mapInQuotationResponse(quotationCreate,responseRimac,nextId);
		LOGGER.info("***** RBVDR403Impl - executeCreateQuotation() - response: {} ***",response);
		LOGGER.info("***** RBVDR403Impl - executeCreateQuotation() - response.ContactDetails: {} ***",response.getContactDetails());

		SimulationMap simulationMap = new SimulationMap();
		Map<String, Object> argumentsForSaveSimulation = simulationMap.createArgumentsForSaveSimulation(nextId,response, responseRimac,this.applicationConfigurationService);
		LOGGER.info("***** RBVDR403Impl - executeCreateQuotation() - argumentsForSaveSimulation: {} ***", argumentsForSaveSimulation);

		QuotationMap quotationMap = new QuotationMap();
		Map<String, Object> argumentsForSaveQuotation = quotationMap.createArgumentsForSaveQuotation(nextId,response, responseRimac,this.applicationConfigurationService);
		LOGGER.info("***** RBVDR403Impl - executeCreateQuotation() - argumentsForSaveQuotation: {} ***", argumentsForSaveQuotation);

		Map<String, Object> argumentsForSaveSimulationProd = SimulationProductMap.createArgumentsForSaveSimulationProduct(nextId,response,insuranceProductId);
		LOGGER.info("***** RBVDR403Impl - executeCreateQuotation() - argumentsForSaveSimulationProd: {} ***",argumentsForSaveSimulationProd);

		ISimulationDAO iSimulationDAO = new SimulationDAOImpl(this.pisdR402);
		ISimulationProductDAO iSimulationProductDAO = new SimulationProductDAOImpl(this.pisdR402);
		IQuotationDAO iQuotationDAO = new QuotationDAOImpl(this.pisdR402);

        iSimulationDAO.insertSimulation(argumentsForSaveSimulation);
		iSimulationProductDAO.insertSimulationProduct(argumentsForSaveSimulationProd);
		iQuotationDAO.insertQuotation(argumentsForSaveQuotation);

		return response;
	}

	public Map<String,Object> getProductInfo(Map<String, Object> argumentsForGetProductId){
		LOGGER.info("***** executeCreateQuotation - getInsuranceSimulationId START *****");

		IInsuranceProductDAO insuranceProductDAO = new InsuranceProductDAO(pisdR401);
		Map<String,Object> productId = insuranceProductDAO.getInsuranceProductId(argumentsForGetProductId);

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
	public BigDecimal getInsurancePruductId(Map<String, Object> productMap){
		BigDecimal productId = (BigDecimal) productMap.get(ContansUtils.Mapper.FIELD_INSURANCE_PRODUCT_ID);
		LOGGER.info("***** executeCreateQuotation - getInsurancePruductId | productId: {} *****",productId);

		return productId;
	}
	public void validInput(EnterpriseQuotationDTO input){
	if (input.getEmployees().getMonthlyPayrollAmount().getAmount()<= 0.0){
		throw new BusinessException("RBVD10094947", false, "ERROR EL MONTO NO PUEDE SER MENOR A CERO");
	}
	else if (input.getEmployees().getEmployeesNumber()<= 0L){
		throw new BusinessException("RBVD10094946", false, "Error el numero de empleados no puede ser menor a cero");

	}
		List<ParticipantDTO> participant = input.getParticipants();

		// Validar que el tipo de documento de cada participante sea "RUC" usando lambdas
		participant.stream()
				.filter(participants -> !"RUC".equals(participants.getIdentityDocument().getDocumentType().getId()))
				.forEach(participants ->this.addAdviceWithDescription("RBVD10094948",
						"Error el tipo de Documento solo puede ser RUC"));
	}
}
