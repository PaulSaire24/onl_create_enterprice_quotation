package com.bbva.rbvd.lib.r403;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.elara.domain.transaction.Context;
import com.bbva.elara.domain.transaction.ThreadContext;


import com.bbva.elara.utility.api.connector.APIConnector;
import com.bbva.pisd.dto.insurance.amazon.SignatureAWS;
import com.bbva.pisd.lib.r014.PISDR014;
import com.bbva.pisd.lib.r401.PISDR401;
import com.bbva.pisd.lib.r402.PISDR402;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.*;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.rimac.*;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.dto.CreateQuotationDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.InsuranceEnterpriseResponseBO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.QuotationBO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.QuotationResponseBO;
import com.bbva.rbvd.lib.r403.impl.RBVDR403Impl;

import com.bbva.rbvd.lib.r403.service.dao.PlanDAO;
import com.bbva.rbvd.lib.r403.service.impl.ConsumerExternalService;
import com.bbva.rbvd.lib.r403.utils.ContansUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestClientException;


import javax.ws.rs.HttpMethod;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RBVDR403Test {

	@Spy
	private Context context;
	@InjectMocks
	private RBVDR403Impl rbvdR302 ;
	@Mock
	private PISDR014 pisdr014 ;
	@Mock
	private APIConnector externalAPIConector ;
	@Mock
	private ApplicationConfigurationService applicationConfigurationService;

	@Mock
	private PISDR401 pisdr401;
	@Mock
	private ConsumerExternalService consumerExternalServiceMock;
	@Mock
	private PISDR402 pisdr402;
	private Map<String, Object> responseQueryModalities;
	private Map<String, Object> responseQuery;

	private CreateQuotationDTO requestInput;
	@Before
	public void setUp() throws Exception {

		context = new Context();
		ThreadContext.set(context);
		requestInput=new CreateQuotationDTO();
	}
	@Test
	public void executeTestOk(){
		this.requestInput =createInput();
		QuotationResponseBO responseRimacMock = createRimacResponse(); // DTO establecido en el test
		InsuranceEnterpriseResponseBO payload = new InsuranceEnterpriseResponseBO();
		payload.setPayload(responseRimacMock);
		when(consumerExternalServiceMock.callRimacService(any(), any(), any(), any())).thenReturn(payload);
		when(externalAPIConector.postForObject(anyString(), any(), any())).thenReturn(payload);

		responseQueryModalities = new HashMap<>();
		responseQueryModalities.put(ContansUtils.Querys.FIELD_Q_PISD_SIMULATION_ID0_NEXTVAL, new BigDecimal(1));
		when(pisdr402.executeGetASingleRow(anyString(), anyMap()))
				.thenReturn(responseQueryModalities);
		when(pisdr402.executeInsertSingleRow(anyString(), anyMap()))
				.thenReturn(1);
		when(pisdr014.executeSignatureConstruction(anyString(), any(), any(), any(), any())).thenReturn(new SignatureAWS());
		when(pisdr401.executeGetProductById(anyString(), anyMap()))
				.thenReturn(1);

		rbvdR302.executeCreateQuotation(requestInput,"PISD","P012341","P012341","0241","a");
	}
	@Test
	public void executeTestRimacFail(){
		this.requestInput =createInput();
		QuotationResponseBO responseRimacMock = createRimacResponse(); // DTO establecido en el test
		InsuranceEnterpriseResponseBO payload = new InsuranceEnterpriseResponseBO();
		payload.setPayload(responseRimacMock);
		when(consumerExternalServiceMock.callRimacService(any(), any(), any(), any())).thenReturn(payload);
		when(externalAPIConector.postForObject(anyString(), any(), any())).thenThrow(new RestClientException(""));

		responseQueryModalities = new HashMap<>();
		responseQueryModalities.put(ContansUtils.Querys.FIELD_Q_PISD_SIMULATION_ID0_NEXTVAL, new BigDecimal(1));
		when(pisdr402.executeGetASingleRow(anyString(), anyMap()))
				.thenReturn(responseQueryModalities);
		when(pisdr402.executeInsertSingleRow(anyString(), anyMap()))
				.thenReturn(1);
		when(pisdr014.executeSignatureConstruction(anyString(), any(), any(), any(), any())).thenReturn(new SignatureAWS());

		try {
			rbvdR302.executeCreateQuotation(requestInput, "PISD", "P012341", "P012341", "0241", "a");
		}
		catch (BusinessException e){

		}}
	@Test
	public void executeTestKO(){
		this.requestInput =createInput();
		QuotationResponseBO responseRimacMock = createRimacResponse(); // DTO establecido en el test
		InsuranceEnterpriseResponseBO payload = new InsuranceEnterpriseResponseBO();
		payload.setPayload(responseRimacMock);
		when(consumerExternalServiceMock.callRimacService(any(), any(), any(), any())).thenReturn(payload);
		when(externalAPIConector.postForObject(anyString(), any(), any())).thenReturn(payload);

		responseQueryModalities = new HashMap<>();
		responseQueryModalities.put(ContansUtils.Querys.FIELD_Q_PISD_SIMULATION_ID0_NEXTVAL, new BigDecimal(1));
		when(pisdr402.executeGetASingleRow(anyString(), anyMap()))
				.thenReturn(responseQueryModalities);
		when(pisdr402.executeInsertSingleRow(anyString(), anyMap()))
				.thenReturn(0);
		when(pisdr014.executeSignatureConstruction(anyString(), any(), any(), any(), any())).thenReturn(new SignatureAWS());
try {
	rbvdR302.executeCreateQuotation(requestInput, "PISD", "P012341", "P012341", "0241", "a");
}
catch (BusinessException e){

}
}
	@Test
	public void executeTestOkfULLOBL(){
		this.requestInput =createInput();
		QuotationResponseBO responseRimacMock = createRimacResponseOBL();
		InsuranceEnterpriseResponseBO payload = new InsuranceEnterpriseResponseBO();
		payload.setPayload(responseRimacMock);
		// DTO establecido en el test
		when(consumerExternalServiceMock.callRimacService(any(), any(), any(), any())).thenReturn(payload);
		when(externalAPIConector.postForObject(anyString(), any(), any())).thenReturn(payload);

		responseQueryModalities = new HashMap<>();
		responseQueryModalities.put(ContansUtils.Querys.FIELD_Q_PISD_SIMULATION_ID0_NEXTVAL, new BigDecimal(1));
		when(pisdr402.executeGetASingleRow(anyString(), anyMap()))
				.thenReturn(responseQueryModalities);
		when(pisdr402.executeInsertSingleRow(anyString(), anyMap()))
				.thenReturn(1);
		when(pisdr014.executeSignatureConstruction(anyString(), any(), any(), any(), any())).thenReturn(new SignatureAWS());

		rbvdR302.executeCreateQuotation(requestInput,"PISD","P012341","P012341","0241","a");
	}
	@Test
	public void executeTestOkfULLINC(){
		this.requestInput =createInput();
		QuotationResponseBO responseRimacMock = createRimacResponseINC();
		InsuranceEnterpriseResponseBO payload = new InsuranceEnterpriseResponseBO();
		payload.setPayload(responseRimacMock);
		// DTO establecido en el test
		when(consumerExternalServiceMock.callRimacService(any(), any(), any(), any())).thenReturn(payload);
		when(externalAPIConector.postForObject(anyString(), any(), any())).thenReturn(payload);

		responseQueryModalities = new HashMap<>();
		responseQueryModalities.put(ContansUtils.Querys.FIELD_Q_PISD_SIMULATION_ID0_NEXTVAL, new BigDecimal(1));
		when(pisdr402.executeGetASingleRow(anyString(), anyMap()))
				.thenReturn(responseQueryModalities);
		when(pisdr402.executeInsertSingleRow(anyString(), anyMap()))
				.thenReturn(1);
		when(pisdr014.executeSignatureConstruction(anyString(), any(), any(), any(), any())).thenReturn(new SignatureAWS());

		rbvdR302.executeCreateQuotation(requestInput,"PISD","P012341","P012341","0241","a");
	}
	@Test
	public void executeTestOkfULLOPC(){
		this.requestInput =createInput();
		QuotationResponseBO responseRimacMock = createRimacResponseOPC();
		InsuranceEnterpriseResponseBO payload = new InsuranceEnterpriseResponseBO();
		payload.setPayload(responseRimacMock);
		// DTO establecido en el test
		when(consumerExternalServiceMock.callRimacService(any(), any(), any(), any())).thenReturn(payload);
		when(externalAPIConector.postForObject(anyString(), any(), any())).thenReturn(payload);

		responseQueryModalities = new HashMap<>();
		responseQueryModalities.put(ContansUtils.Querys.FIELD_Q_PISD_SIMULATION_ID0_NEXTVAL, new BigDecimal(1));
		when(pisdr402.executeGetASingleRow(anyString(), anyMap()))
				.thenReturn(responseQueryModalities);
		when(pisdr402.executeInsertSingleRow(anyString(), anyMap()))
				.thenReturn(1);
		when(pisdr014.executeSignatureConstruction(anyString(), any(), any(), any(), any())).thenReturn(new SignatureAWS());

		rbvdR302.executeCreateQuotation(requestInput,"PISD","P012341","P012341","0241","a");
	}

	private QuotationResponseBO createRimacResponse(){
		QuotationResponseBO responseBO = new QuotationResponseBO();

		responseBO.setProducto("842");
		List<PlanBO> planes = new ArrayList<>();
		List<Long> planes2 = new ArrayList<>();
		planes2.add(1234124l);
		PlanBO plan1 = new PlanBO();
		PlanBO plan2 = new PlanBO();
		List<FinancingBO> financingBOList = new ArrayList<>();
		FinancingBO financing = new FinancingBO();
		financing.setPeriodicidad("1");
		financing.setNumeroCuotas(1l);
		List<InstallmentFinancingBO> installmentFinancingBOS = new ArrayList<>();
		InstallmentFinancingBO installment1 = new InstallmentFinancingBO();
		installment1.setFechaVencimiento("20-11-2022");
		installment1.setMonto(new BigDecimal(1000));
		installment1.setCuota(1l);
		installment1.setMoneda("PEN");
		installmentFinancingBOS.add(installment1);

		financing.setCuotasFinanciamiento(installmentFinancingBOS);
		financing.setFinanciamiento(1l);
		financingBOList.add(financing);
		List<CoverageBO> coverageBOList = new ArrayList<>();
		CoverageBO coverageBO = new CoverageBO();
		coverageBO.setCondicion("1");
		coverageBO.setMoneda("pen");
		coverageBO.setCobertura(1l);

		plan1.setPlan(1l);
		plan1.setFinanciamientos(financingBOList);
		plan1.setPrimaNeta(new BigDecimal(1000));
		plan1.setMoneda("pen");

		plan2.setPlan(2l);
		plan2.setFinanciamientos(financingBOList);
		plan2.setPrimaNeta(new BigDecimal(1000));
		plan2.setMoneda("pen");

		List<QuotationBO> cotizaciones = new ArrayList<>();
		QuotationBO cotizaciones1 = new QuotationBO();
		QuotationBO cotizaciones2 = new QuotationBO();
		planes.add(plan1);
		planes.add(plan2);
		cotizaciones1.setPlan(plan1);
		cotizaciones1.setFechaFinVigencia("2024-04-30");
		cotizaciones2.setPlan(plan2);
		cotizaciones.add(cotizaciones1);
		cotizaciones.add(cotizaciones2);
		responseBO.setPlanes(planes2);
		responseBO.setCotizaciones(cotizaciones);
		return responseBO;
	}
	private QuotationResponseBO createRimacResponseOBL(){
		QuotationResponseBO responseBO = new QuotationResponseBO();
		responseBO.setProducto("842");
		List<Long> planes2 = new ArrayList<>();
		planes2.add(1234124l);
		List<PlanBO> planes = new ArrayList<>();
		PlanBO plan1 = new PlanBO();
		PlanBO plan2 = new PlanBO();
		List<FinancingBO> financingBOList = new ArrayList<>();
		FinancingBO financing = new FinancingBO();
		financing.setPeriodicidad("1");
		financing.setNumeroCuotas(1l);
		List<InstallmentFinancingBO> installmentFinancingBOS = new ArrayList<>();
		InstallmentFinancingBO installment1 = new InstallmentFinancingBO();
		installment1.setFechaVencimiento("20-11-2022");
		installment1.setMonto(new BigDecimal(1000));
		installment1.setCuota(1l);
		installment1.setMoneda("PEN");
		installmentFinancingBOS.add(installment1);

		financing.setCuotasFinanciamiento(installmentFinancingBOS);
		financing.setFinanciamiento(1l);
		financingBOList.add(financing);
		List<CoverageBO> coverageBOList = new ArrayList<>();
		CoverageBO coverageBO = new CoverageBO();
		coverageBO.setCondicion("OBL");
		coverageBO.setMoneda("pen");
		coverageBO.setCobertura(1l);
		coverageBOList.add(coverageBO);
		plan1.setPlan(1l);
		plan1.setCoberturas(coverageBOList);
		plan1.setFinanciamientos(financingBOList);
		plan1.setPrimaNeta(new BigDecimal(1000));
		plan1.setMoneda("pen");
		plan2.setPlan(2l);
		plan2.setFinanciamientos(financingBOList);
		plan2.setPrimaNeta(new BigDecimal(1000));
		plan2.setMoneda("pen");

		List<QuotationBO> cotizaciones = new ArrayList<>();
		QuotationBO cotizaciones1 = new QuotationBO();
		QuotationBO cotizaciones2 = new QuotationBO();
		planes.add(plan1);
		planes.add(plan2);
		cotizaciones1.setPlan(plan1);
		cotizaciones1.setFechaFinVigencia("2024-04-30");
		cotizaciones2.setPlan(plan2);
		cotizaciones.add(cotizaciones1);
		cotizaciones.add(cotizaciones2);
		planes.add(plan1);
		responseBO.setCotizaciones(cotizaciones);
		responseBO.setPlanes(planes2);
		return responseBO;
	}
	private QuotationResponseBO createRimacResponseINC(){
		QuotationResponseBO responseBO = new QuotationResponseBO();
		responseBO.setProducto("842");
		List<PlanBO> planes = new ArrayList<>();
		List<Long> planes2 = new ArrayList<>();
		planes2.add(1234124l);
		PlanBO plan1 = new PlanBO();
		List<AssistanceBO> assistanceBOList = new ArrayList<>();
		AssistanceBO asistencia = new AssistanceBO();
		asistencia.setDescripcionAsistencia("Asistencia 1");
		asistencia.setAsistencia(1l);
		List<FinancingBO> financingBOList = new ArrayList<>();
		FinancingBO financing = new FinancingBO();
		financing.setPeriodicidad("1");
		financing.setNumeroCuotas(1l);
		List<InstallmentFinancingBO> installmentFinancingBOS = new ArrayList<>();
		InstallmentFinancingBO installment1 = new InstallmentFinancingBO();
		installment1.setFechaVencimiento("20-11-2022");
		installment1.setMonto(new BigDecimal(1000));
		installment1.setCuota(1l);
		installment1.setMoneda("PEN");
		installmentFinancingBOS.add(installment1);

		financing.setCuotasFinanciamiento(installmentFinancingBOS);
		financing.setFinanciamiento(1l);
		financingBOList.add(financing);
		List<CoverageBO> coverageBOList = new ArrayList<>();
		CoverageBO coverageBO = new CoverageBO();
		coverageBO.setCondicion("INC");
		coverageBO.setMoneda("pen");
		coverageBO.setCobertura(1l);
		coverageBOList.add(coverageBO);
		plan1.setPlan(1l);
		plan1.setCoberturas(coverageBOList);
		plan1.setFinanciamientos(financingBOList);
		plan1.setPrimaNeta(new BigDecimal(1000));
		plan1.setMoneda("pen");
		plan1.setAsistencias(assistanceBOList);
		planes.add(plan1);
		responseBO.setPlanes(planes2);
		return responseBO;
	}
	private QuotationResponseBO createRimacResponseOPC(){
		QuotationResponseBO responseBO = new QuotationResponseBO();
		responseBO.setProducto("842");
		List<Long> planes2 = new ArrayList<>();
		planes2.add(1234124l);
		List<PlanBO> planes = new ArrayList<>();
		PlanBO plan1 = new PlanBO();
		List<FinancingBO> financingBOList = new ArrayList<>();
		FinancingBO financing = new FinancingBO();
		financing.setPeriodicidad("1");
		financing.setNumeroCuotas(1l);
		List<InstallmentFinancingBO> installmentFinancingBOS = new ArrayList<>();
		InstallmentFinancingBO installment1 = new InstallmentFinancingBO();
		installment1.setFechaVencimiento("20-11-2022");
		installment1.setMonto(new BigDecimal(1000));
		installment1.setCuota(1l);
		installment1.setMoneda("PEN");
		installmentFinancingBOS.add(installment1);

		financing.setCuotasFinanciamiento(installmentFinancingBOS);
		financing.setFinanciamiento(1l);
		financingBOList.add(financing);
		List<CoverageBO> coverageBOList = new ArrayList<>();
		CoverageBO coverageBO = new CoverageBO();
		coverageBO.setCondicion("OPC");
		coverageBO.setMoneda("pen");
		coverageBO.setCobertura(1l);
		coverageBOList.add(coverageBO);
		plan1.setPlan(1l);
		plan1.setCoberturas(coverageBOList);
		plan1.setFinanciamientos(financingBOList);
		plan1.setPrimaNeta(new BigDecimal(1000));
		plan1.setMoneda("pen");
		planes.add(plan1);
		responseBO.setPlanes(planes2);
		return responseBO;
	}
	private CreateQuotationDTO createInput(){
		CreateQuotationDTO input = new CreateQuotationDTO();
		ProductDTO product = new ProductDTO();
		List<Long> planes2 = new ArrayList<>();
		planes2.add(1234124l);
		List<ContactDetailsDTO> contactDetails = new ArrayList<>();
		List<ParticipantDTO> participantes = new ArrayList<>();
		ContactDetailsDTO contacto1 = new ContactDetailsDTO();
		ContactDTO contacto = new ContactDTO();
		ParticipantDTO participnt1 = new ParticipantDTO();
		DescriptionDTO participantType = new DescriptionDTO();
		EmployeesDTO employees = new EmployeesDTO();
		DescriptionDTO busunessAgent = new DescriptionDTO();

		participnt1.setId("P041360");
		IdentityDocumentDTO document = new IdentityDocumentDTO();
		DescriptionDTO documentType = new DescriptionDTO();
		document.setDocumentNumber("73186739");
		documentType.setId("DNI");

		document.setDocumentType(documentType);
		participnt1.setIdentityDocument(document);
		participantType.setId("123456");
		participantType.setName("Contract");
		participnt1.setParticipantType(participantType);
		participantes.add(participnt1);
		busunessAgent.setId("P021322");
		employees.setAreMajorityAge(true);
		employees.setEmployeesNumber(Long.valueOf(30));
		AmountDTO monthlyPayrollAmount = new AmountDTO();
		monthlyPayrollAmount.setCurrency("PEN");
		monthlyPayrollAmount.setAmount(BigDecimal.valueOf(20.00).doubleValue());
		employees.setMonthlyPayrollAmount((monthlyPayrollAmount));
		product.setId("503");
		contacto.setContactDetailType("EMAIL");
		contacto.setAddress("marco.yovera@bbva.com");
		contacto1.setContact(contacto);
		contactDetails.add(contacto1);

		input.setProduct(product);
		input.setParticipants(participantes);
		input.setQuotationReference("2312313");
		input.setEmployees(employees);
		input.setBusinessAgent(busunessAgent);
		input.setContactDetails(contactDetails);


		return input;
	}
}
