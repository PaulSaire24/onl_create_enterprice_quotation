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
import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.AmountDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.BankDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.ContactDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.ContactDetailsDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.DescriptionDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.EmployeesDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.EnterpriseQuotationDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.IdentityDocumentDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.ParticipantDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.PaymentMethodDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.ProductDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.rimac.PlanBO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.rimac.CoverageBO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.rimac.FinancingBO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.rimac.ParticularDataBO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.rimac.AssistanceBO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.rimac.TaxBO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.rimac.InstallmentFinancingBO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.InsuranceEnterpriseResponseBO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.QuotationBO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.QuotationResponseBO;
import com.bbva.rbvd.dto.enterpriseinsurance.utils.ConstantsUtil;
import com.bbva.rbvd.lib.r403.impl.RBVDR403Impl;

import com.bbva.rbvd.lib.r403.service.api.ConsumerExternalService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestClientException;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.*;
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
	private List<Map<String, Object>> responseQueryPlans;
	private Map<String, Object> responseQuery;

	private EnterpriseQuotationDTO requestInput;
	@Before
	public void setUp() throws Exception {

		context = new Context();
		ThreadContext.set(context);
		requestInput = new EnterpriseQuotationDTO();
	}
	@Test
	public void executeTestOk(){
		this.requestInput = createInput();
		QuotationResponseBO responseRimacMock = createRimacResponse(); // DTO establecido en el test
		InsuranceEnterpriseResponseBO payload = new InsuranceEnterpriseResponseBO();
		payload.setPayload(responseRimacMock);
		when(consumerExternalServiceMock.callRimacService(any(), any(), any(), any())).thenReturn(payload);
		when(externalAPIConector.postForObject(anyString(), any(), any())).thenReturn(payload);
		List<Map<String, Object>> listPlan = createPlan();
		responseQueryModalities = new HashMap<>();
		responseQueryModalities.put(ConstantsUtil.QueriesName.FIELD_Q_PISD_SIMULATION_ID0_NEXTVAL, new BigDecimal(1));
		when(pisdr402.executeGetASingleRow(anyString(), anyMap()))
				.thenReturn(responseQueryModalities);
		when(pisdr402.executeInsertSingleRow(anyString(), anyMap()))
				.thenReturn(1);
		when(pisdr402.executeGetListASingleRow(anyString(), anyMap()))
				.thenReturn(listPlan);
		when(pisdr014.executeSignatureConstruction(anyString(), any(), any(), any(), any())).thenReturn(new SignatureAWS());
		when(pisdr401.executeGetProductById(anyString(), anyMap()))
				.thenReturn(createProduct());

		EnterpriseQuotationDTO response = rbvdR302.executeCreateQuotation(requestInput);

		assertNotNull(response);

	}
	@Test(expected = BusinessException.class)
	public void executeTestRimacFail(){
		this.requestInput =createInputQuotationReference();
		QuotationResponseBO responseRimacMock = createRimacResponse(); // DTO establecido en el test
		InsuranceEnterpriseResponseBO payload = new InsuranceEnterpriseResponseBO();
		payload.setPayload(responseRimacMock);
		when(consumerExternalServiceMock.callRimacService(any(), any(), any(), any())).thenReturn(payload);
		when(externalAPIConector.postForObject(anyString(), any(), any())).thenThrow(new RestClientException(""));

		responseQueryModalities = new HashMap<>();
		responseQueryModalities.put(ConstantsUtil.QueriesName.FIELD_Q_PISD_SIMULATION_ID0_NEXTVAL, new BigDecimal(1));
		when(pisdr402.executeGetASingleRow(anyString(), anyMap()))
				.thenReturn(responseQueryModalities);
		when(pisdr401.executeGetProductById(anyString(), anyMap()))
				.thenReturn(createProduct());
		when(pisdr402.executeInsertSingleRow(anyString(), anyMap()))
				.thenReturn(1);
		when(pisdr014.executeSignatureConstruction(anyString(), any(), any(), any(), any())).thenReturn(new SignatureAWS());
		when(pisdr402.executeGetListASingleRow("PISD.GET_MODALITY_TYPE_BY_PRODUCT_ID",getArgumentsPlans()))
				.thenReturn(createPlan());
		when(pisdr402.executeGetListASingleRow("PISD.GET_QUOTATION_POLICY_ID",getArgumentsPolicy()))
				.thenReturn(getPolicy());
		EnterpriseQuotationDTO responnse =	 rbvdR302.executeCreateQuotation(requestInput);

		assertNotNull(responnse);

	}
	@Test(expected = BusinessException.class)
	public void executeTestDNI(){
		this.requestInput = createInputDNI();
		QuotationResponseBO responseRimacMock = createRimacResponse(); // DTO establecido en el test
		InsuranceEnterpriseResponseBO payload = new InsuranceEnterpriseResponseBO();
		payload.setPayload(responseRimacMock);
		when(consumerExternalServiceMock.callRimacService(any(), any(), any(), any())).thenReturn(payload);
		when(externalAPIConector.postForObject(anyString(), any(), any())).thenReturn(payload);
		List<Map<String, Object>> listPlan = createPlan();
		responseQueryModalities = new HashMap<>();
		responseQueryModalities.put(ConstantsUtil.QueriesName.FIELD_Q_PISD_SIMULATION_ID0_NEXTVAL, new BigDecimal(1));
		when(pisdr402.executeGetASingleRow(anyString(), anyMap()))
				.thenReturn(responseQueryModalities);
		when(pisdr402.executeInsertSingleRow(anyString(), anyMap()))
				.thenReturn(1);
		when(pisdr402.executeGetListASingleRow(anyString(), anyMap()))
				.thenReturn(listPlan);
		when(pisdr014.executeSignatureConstruction(anyString(), any(), any(), any(), any())).thenReturn(new SignatureAWS());
		when(pisdr401.executeGetProductById(anyString(), anyMap()))
				.thenReturn(createProduct());
		EnterpriseQuotationDTO responnse =	 rbvdR302.executeCreateQuotation(requestInput);

		assertNotNull(responnse);
	}
	@Test(expected = BusinessException.class)
	public void executeTestKO(){
		this.requestInput =createInputQuotationReference();
		QuotationResponseBO responseRimacMock = createRimacResponse(); // DTO establecido en el test
		InsuranceEnterpriseResponseBO payload = new InsuranceEnterpriseResponseBO();
		payload.setPayload(responseRimacMock);
		when(consumerExternalServiceMock.callRimacService(any(), any(), any(), any())).thenReturn(payload);
		when(externalAPIConector.postForObject(anyString(), any(), any())).thenReturn(payload);

		responseQueryModalities = new HashMap<>();
		responseQueryModalities.put(ConstantsUtil.QueriesName.FIELD_Q_PISD_SIMULATION_ID0_NEXTVAL, new BigDecimal(1));
		when(pisdr402.executeGetASingleRow(anyString(), anyMap()))
				.thenReturn(responseQueryModalities);
		when(pisdr401.executeGetProductById(anyString(), anyMap()))
				.thenReturn(createProduct());
		when(pisdr402.executeInsertSingleRow(anyString(), anyMap()))
				.thenReturn(0);
		when(pisdr402.executeGetListASingleRow("PISD.GET_MODALITY_TYPE_BY_PRODUCT_ID",getArgumentsPlans()))
				.thenReturn(createPlan());
		when(pisdr402.executeGetListASingleRow("PISD.GET_QUOTATION_POLICY_ID",getArgumentsPolicy()))
				.thenReturn(getPolicy());
		when(pisdr014.executeSignatureConstruction(anyString(), any(), any(), any(), any())).thenReturn(new SignatureAWS());
		EnterpriseQuotationDTO responnse =	 rbvdR302.executeCreateQuotation(requestInput);

		assertNotNull(responnse);
}
	@Test
	public void executeTestOkfULLOBL(){
		this.requestInput =createInputQuotationReference();
		QuotationResponseBO responseRimacMock = createRimacResponseOBL();
		InsuranceEnterpriseResponseBO payload = new InsuranceEnterpriseResponseBO();
		payload.setPayload(	responseRimacMock);
		// DTO establecido en el test
		when(consumerExternalServiceMock.callRimacService(any(), any(), any(), any())).thenReturn(payload);
		when(externalAPIConector.postForObject(anyString(), any(), any())).thenReturn(payload);

		responseQueryModalities = new HashMap<>();
		responseQueryModalities.put(ConstantsUtil.QueriesName.FIELD_Q_PISD_SIMULATION_ID0_NEXTVAL, new BigDecimal(1));
		when(pisdr402.executeGetASingleRow(anyString(), anyMap()))
				.thenReturn(responseQueryModalities);
		when(pisdr402.executeInsertSingleRow(anyString(), anyMap()))
				.thenReturn(1);
		when(pisdr401.executeGetProductById(anyString(), anyMap()))
				.thenReturn(createProduct());
		when(pisdr014.executeSignatureConstruction(anyString(), any(), any(), any(), any())).thenReturn(new SignatureAWS());
		when(pisdr402.executeGetListASingleRow("PISD.GET_MODALITY_TYPE_BY_PRODUCT_ID",getArgumentsPlans()))
				.thenReturn(createPlan());
		when(pisdr402.executeGetListASingleRow("PISD.GET_QUOTATION_POLICY_ID",getArgumentsPolicy()))
				.thenReturn(getPolicy());
		EnterpriseQuotationDTO response = rbvdR302.executeCreateQuotation(requestInput);
		assertNotNull(response);
		assertEquals(response.getProduct().getPlans().get(0).getId(),"00");
		assertEquals(response.getProduct().getPlans().get(1).getId(),"01");

	}
	@Test
	public void executeTestOkfULLINC(){
		this.requestInput =createInputQuotationReference();
		QuotationResponseBO responseRimacMock = createRimacResponseINC();
		InsuranceEnterpriseResponseBO payload = new InsuranceEnterpriseResponseBO();
		payload.setPayload(responseRimacMock);
		// DTO establecido en el test
		when(consumerExternalServiceMock.callRimacService(any(), any(), any(), any())).thenReturn(payload);
		when(externalAPIConector.postForObject(anyString(), any(), any())).thenReturn(payload);

		responseQueryModalities = new HashMap<>();
		responseQueryModalities.put(ConstantsUtil.QueriesName.FIELD_Q_PISD_SIMULATION_ID0_NEXTVAL, new BigDecimal(1));
		when(pisdr402.executeGetASingleRow(anyString(), anyMap()))
				.thenReturn(responseQueryModalities);
		when(pisdr401.executeGetProductById(anyString(), anyMap()))
				.thenReturn(createProduct());
		when(pisdr402.executeInsertSingleRow(anyString(), anyMap()))
				.thenReturn(1);
		when(pisdr402.executeGetListASingleRow("PISD.GET_MODALITY_TYPE_BY_PRODUCT_ID",getArgumentsPlans()))
				.thenReturn(createPlan());
		when(pisdr402.executeGetListASingleRow("PISD.GET_QUOTATION_POLICY_ID",getArgumentsPolicy()))
				.thenReturn(getPolicy());
		when(pisdr014.executeSignatureConstruction(anyString(), any(), any(), any(), any())).thenReturn(new SignatureAWS());

		EnterpriseQuotationDTO response = rbvdR302.executeCreateQuotation(requestInput);
		assertNotNull(response);
		assertEquals(response.getProduct().getPlans().get(0).getId(),"00");
		assertEquals(response.getProduct().getPlans().get(1).getId(),"00");

	}
	@Test
	public void executeTestOkfULLOPC(){
		this.requestInput =createInputQuotationReference();
		QuotationResponseBO responseRimacMock = createRimacResponseOPC();
		InsuranceEnterpriseResponseBO payload = new InsuranceEnterpriseResponseBO();
		payload.setPayload(responseRimacMock);
		// DTO establecido en el test
		when(consumerExternalServiceMock.callRimacService(any(), any(), any(), any())).thenReturn(payload);
		when(externalAPIConector.postForObject(anyString(), any(), any())).thenReturn(payload);

		responseQueryModalities = new HashMap<>();
		responseQueryModalities.put(ConstantsUtil.QueriesName.FIELD_Q_PISD_SIMULATION_ID0_NEXTVAL, new BigDecimal(1));
		when(pisdr402.executeGetASingleRow(anyString(), anyMap()))
				.thenReturn(responseQueryModalities);
		when(pisdr401.executeGetProductById(anyString(), anyMap()))
				.thenReturn(createProduct());
		when(pisdr402.executeInsertSingleRow(anyString(), anyMap()))
				.thenReturn(1);
		when(pisdr402.executeGetListASingleRow("PISD.GET_MODALITY_TYPE_BY_PRODUCT_ID",getArgumentsPlans()))
				.thenReturn(createPlan());
		when(pisdr402.executeGetASingleRow("PISD.SELECT_QUOTATION_BY_PLAN",getArgumentsPlanSelected()))
				.thenReturn(createPlanSelected());
		when(pisdr402.executeGetListASingleRow("PISD.GET_QUOTATION_POLICY_ID",getArgumentsPolicy()))
				.thenReturn(getPolicy());

		when(pisdr014.executeSignatureConstruction(anyString(), any(), any(), any(), any())).thenReturn(new SignatureAWS());

		EnterpriseQuotationDTO response = rbvdR302.executeCreateQuotation(requestInput);
		assertNotNull(response);
		assertEquals(response.getProduct().getPlans().get(0).getId(),"00");
		assertEquals(response.getProduct().getPlans().get(1).getId(),"01");
		assertEquals(response.getProduct().getPlans().get(1).getName(),"PLAN SOLES");

	}
	@Test(expected = BusinessException.class)
	public void executeTestAddAdvice1(){
		this.requestInput = createInputAmountZero();
		QuotationResponseBO responseRimacMock = createRimacResponse(); // DTO establecido en el test
		InsuranceEnterpriseResponseBO payload = new InsuranceEnterpriseResponseBO();
		payload.setPayload(responseRimacMock);
		when(consumerExternalServiceMock.callRimacService(any(), any(), any(), any())).thenReturn(payload);
		when(externalAPIConector.postForObject(anyString(), any(), any())).thenReturn(payload);
		List<Map<String, Object>> listPlan = createPlan();
		responseQueryModalities = new HashMap<>();
		responseQueryModalities.put(ConstantsUtil.QueriesName.FIELD_Q_PISD_SIMULATION_ID0_NEXTVAL, new BigDecimal(1));
		when(pisdr402.executeGetASingleRow(anyString(), anyMap()))
				.thenReturn(responseQueryModalities);
		when(pisdr402.executeInsertSingleRow(anyString(), anyMap()))
				.thenReturn(1);
		when(pisdr402.executeGetListASingleRow(anyString(), anyMap()))
				.thenReturn(listPlan);
		when(pisdr014.executeSignatureConstruction(anyString(), any(), any(), any(), any())).thenReturn(new SignatureAWS());
		when(pisdr401.executeGetProductById(anyString(), anyMap()))
				.thenReturn(createProduct());

		EnterpriseQuotationDTO responnse =	 rbvdR302.executeCreateQuotation(requestInput);

		assertNotNull(responnse);
	}
	@Test(expected = BusinessException.class)
	public void executeTestAddAdvice2(){
		this.requestInput = createInputNumberEmployeesZero();
		QuotationResponseBO responseRimacMock = createRimacResponse(); // DTO establecido en el test
		InsuranceEnterpriseResponseBO payload = new InsuranceEnterpriseResponseBO();
		payload.setPayload(responseRimacMock);
		when(consumerExternalServiceMock.callRimacService(any(), any(), any(), any())).thenReturn(payload);
		when(externalAPIConector.postForObject(anyString(), any(), any())).thenReturn(payload);
		List<Map<String, Object>> listPlan = createPlan();
		responseQueryModalities = new HashMap<>();
		responseQueryModalities.put(ConstantsUtil.QueriesName.FIELD_Q_PISD_SIMULATION_ID0_NEXTVAL, new BigDecimal(1));
		when(pisdr402.executeGetASingleRow(anyString(), anyMap()))
				.thenReturn(responseQueryModalities);
		when(pisdr402.executeInsertSingleRow(anyString(), anyMap()))
				.thenReturn(1);
		when(pisdr402.executeGetListASingleRow(anyString(), anyMap()))
				.thenReturn(listPlan);
		when(pisdr014.executeSignatureConstruction(anyString(), any(), any(), any(), any())).thenReturn(new SignatureAWS());
		when(pisdr401.executeGetProductById(anyString(), anyMap()))
				.thenReturn(createProduct());

		EnterpriseQuotationDTO responnse =	 rbvdR302.executeCreateQuotation(requestInput);

		assertNotNull(responnse);
	}
	private QuotationResponseBO createRimacResponse(){
		QuotationResponseBO responseBO = new QuotationResponseBO();

		responseBO.setProducto("842");
		List<Long> planes2 = new ArrayList<>();
		planes2.add(1234124l);
		List<ParticularDataBO> datosParticulares = new ArrayList<>();
		ParticularDataBO valores = new ParticularDataBO();
		valores.setValor("10000");
		valores.setEtiqueta("SUMA_ASEGURADA");
		datosParticulares.add(valores);
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
		coverageBO.setPrincipal("S");
		coverageBOList.add(coverageBO);
		plan1.setPlan(534254L);
		plan1.setFinanciamientos(financingBOList);
		plan1.setPrimaNeta(new BigDecimal(1000));
		plan1.setPrimaBruta(new BigDecimal(1000));
		plan1.setMoneda("pen");
		plan1.setCoberturas(coverageBOList);
		plan1.setDescripcionPlan("PLAN PLATA SOLES 10000");
		/*TaxBO taxBO1 = new TaxBO();
		taxBO1.setTasa(new BigDecimal(18));
		taxBO1.setDescripcion("Rango de 18-64 años");
		taxBO1.setRango("0");
		plan1.setTasas(Collections.singletonList(taxBO1));*/
		plan2.setPlan(534254L);
		plan2.setFinanciamientos(financingBOList);
		plan2.setPrimaNeta(new BigDecimal(1000));
		plan2.setPrimaBruta(new BigDecimal(1000));
		plan2.setMoneda("pen");
		plan2.setCoberturas(coverageBOList);
		plan2.setDescripcionPlan("PLAN PLATA SOLES 10000");
		/*TaxBO taxBO2 = new TaxBO();
		taxBO2.setTasa(new BigDecimal(18));
		taxBO2.setDescripcion("Rango de 18-64 años");
		taxBO2.setRango("0");
		plan2.setTasas(Collections.singletonList(taxBO2));*/
		List<QuotationBO> cotizaciones = new ArrayList<>();
		QuotationBO cotizaciones1 = new QuotationBO();
		QuotationBO cotizaciones2 = new QuotationBO();
		cotizaciones1.setPlan(plan1);
		cotizaciones1.setFechaFinVigencia("2024-04-30");
		cotizaciones2.setPlan(plan2);
		cotizaciones.add(cotizaciones1);
		cotizaciones.add(cotizaciones2);
		responseBO.setPlanes(planes2);
		responseBO.setCotizaciones(cotizaciones);
		responseBO.setDatosParticulares(datosParticulares);
		return responseBO;
	}
	private QuotationResponseBO createRimacResponseOBL(){
		QuotationResponseBO responseBO = new QuotationResponseBO();
		responseBO.setProducto("842");
		List<PlanBO> planes = new ArrayList<>();
		List<Long> planes2 = new ArrayList<>();
		planes2.add(1234124l);
		List<ParticularDataBO> datosParticulares = new ArrayList<>();
		ParticularDataBO valores = new ParticularDataBO();
		valores.setValor("10000");
		valores.setEtiqueta("SUMA_ASEGURADA");
		datosParticulares.add(valores);
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
		plan1.setPlan(534254L);
		plan1.setCoberturas(coverageBOList);
		plan1.setFinanciamientos(financingBOList);
		plan1.setPrimaNeta(new BigDecimal(1000));
		plan1.setPrimaBruta(new BigDecimal(1000));
		plan1.setMoneda("pen");
		plan1.setDescripcionPlan("PLAN PLATA SOLES 10000");

		TaxBO taxBO1 = new TaxBO();
		taxBO1.setTasa(new BigDecimal(18));
		taxBO1.setDescripcion("Rango de 18-64 años");
		taxBO1.setRango("0");
		plan1.setTasas(Collections.singletonList(taxBO1));

		plan2.setPlan(2l);
		plan2.setFinanciamientos(financingBOList);
		plan2.setPrimaNeta(new BigDecimal(1000));
		plan2.setPrimaBruta(new BigDecimal(1000));
		plan2.setMoneda("pen");
		plan2.setDescripcionPlan("PLAN PLATA SOLES 10000");
		plan2.setCoberturas(coverageBOList);

		TaxBO taxBO2 = new TaxBO();
		taxBO2.setTasa(new BigDecimal(18));
		taxBO2.setDescripcion("Rango de 18-64 años");
		taxBO2.setRango("0");
		plan2.setTasas(Collections.singletonList(taxBO2));

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
		cotizaciones1.setPlan(plan1);
		responseBO.setDatosParticulares(datosParticulares);
		responseBO.setCotizaciones(cotizaciones);
		responseBO.setPlanes(planes2);
		return responseBO;
	}
	private QuotationResponseBO createRimacResponseINC(){
		QuotationResponseBO responseBO = new QuotationResponseBO();
		responseBO.setProducto("842");
		List<PlanBO> planes = new ArrayList<>();
		List<Long> planes2 = new ArrayList<>();
		List<ParticularDataBO> datosParticulares = new ArrayList<>();
		ParticularDataBO valores = new ParticularDataBO();
		valores.setValor("10000");
		valores.setEtiqueta("SUMA_ASEGURADA");
		datosParticulares.add(valores);
		planes2.add(1234124l);
		PlanBO plan1 = new PlanBO();
		PlanBO plan2 = new PlanBO();
		List<AssistanceBO> assistanceBOList = new ArrayList<>();
		AssistanceBO asistencia = new AssistanceBO();
		asistencia.setDescripcionAsistencia("Asistencia 1");
		asistencia.setAsistencia(1l);
		assistanceBOList.add(asistencia);
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
		financing.setFinanciamiento(534273L);
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
		plan1.setPrimaBruta(new BigDecimal(1000));
		plan1.setMoneda("pen");
		plan1.setAsistencias(assistanceBOList);
		plan1.setDescripcionPlan("PLAN PLATA SOLES 10000");

		TaxBO taxBO1 = new TaxBO();
		taxBO1.setTasa(new BigDecimal(18));
		taxBO1.setDescripcion("Rango de 18-64 años");
		taxBO1.setRango("0");
		plan1.setTasas(Collections.singletonList(taxBO1));

		planes.add(plan1);
		plan2.setPlan(2l);
		plan2.setCoberturas(coverageBOList);
		plan2.setFinanciamientos(financingBOList);
		plan2.setPrimaNeta(new BigDecimal(1000));
		plan2.setPrimaBruta(new BigDecimal(1000));
		plan2.setMoneda("pen");
		plan2.setDescripcionPlan("PLAN PLATA SOLES 10000");

		TaxBO taxBO2 = new TaxBO();
		taxBO2.setTasa(new BigDecimal(18));
		taxBO2.setDescripcion("Rango de 18-64 años");
		taxBO2.setRango("0");
		plan2.setTasas(Collections.singletonList(taxBO2));

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
		responseBO.setDatosParticulares(datosParticulares);
		responseBO.setCotizaciones(cotizaciones);
		responseBO.setPlanes(planes2);
		return responseBO;
	}
	private QuotationResponseBO createRimacResponseOPC(){
		QuotationResponseBO responseBO = new QuotationResponseBO();
		responseBO.setProducto("842");
		List<PlanBO> planes = new ArrayList<>();
		List<Long> planes2 = new ArrayList<>();
		planes2.add(1234124l);
		List<ParticularDataBO> datosParticulares = new ArrayList<>();
		ParticularDataBO valores = new ParticularDataBO();
		valores.setValor("10000");
		valores.setEtiqueta("SUMA_ASEGURADA");
		datosParticulares.add(valores);
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
		coverageBO.setCondicion("OPC");
		coverageBO.setMoneda("pen");
		coverageBO.setCobertura(1l);
		coverageBOList.add(coverageBO);
		plan1.setPlan(534254L);
		plan1.setCoberturas(coverageBOList);
		plan1.setFinanciamientos(financingBOList);
		plan1.setPrimaNeta(new BigDecimal(1000));
		plan1.setPrimaBruta(new BigDecimal(1000));
		plan1.setMoneda("pen");
		plan1.setDescripcionPlan("PLAN PLATA SOLES 10000");
		TaxBO taxBO1 = new TaxBO();
		taxBO1.setTasa(new BigDecimal(18));
		taxBO1.setDescripcion("Rango de 18-64 años");
		taxBO1.setRango("0");
		plan1.setTasas(Collections.singletonList(taxBO1));

		planes.add(plan1);
		plan2.setPlan(2l);
		plan2.setFinanciamientos(financingBOList);
		plan2.setPrimaNeta(new BigDecimal(1000));
		plan2.setPrimaBruta(new BigDecimal(1000));
		plan2.setMoneda("pen");
		plan2.setDescripcionPlan("PLAN PLATA SOLES 10000");
		plan2.setCoberturas(coverageBOList);
		TaxBO taxBO2 = new TaxBO();
		taxBO2.setTasa(new BigDecimal(18));
		taxBO2.setDescripcion("Rango de 18-64 años");
		taxBO2.setRango("0");
		plan2.setTasas(Collections.singletonList(taxBO2));

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
		responseBO.setDatosParticulares(datosParticulares);
		responseBO.setPlanes(planes2);
		return responseBO;
	}private EnterpriseQuotationDTO createInputDNI(){
		EnterpriseQuotationDTO input = new EnterpriseQuotationDTO();
		BankDTO bank = new BankDTO();
		PaymentMethodDTO paymentMethodDTO = new PaymentMethodDTO();
		ProductDTO product = new ProductDTO();
		product.setId("842");
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
		monthlyPayrollAmount.setAmount(BigDecimal.valueOf(200000000000000000.00).doubleValue());
		employees.setMonthlyPayrollAmount((monthlyPayrollAmount));
		product.setId("503");
		contacto.setContactDetailType("EMAIL");
		contacto.setAddress("marco.yovera@bbva.com");
		contacto1.setContact(contacto);
		contactDetails.add(contacto1);
        input.setQuotationReference("01720842640900");
		input.setProduct(product);
		input.setParticipants(participantes);
		input.setQuotationReference("2312313");
		input.setEmployees(employees);
		input.setBusinessAgent(busunessAgent);
		input.setContactDetails(contactDetails);
		input.setSaleChannelId("PC");
		input.setUserAudit("zg01293");
		input.setCreationUser("zg01293");
		input.setTraceId("traceId");
		input.setSourceBranchCode("0072");
		input.setLastChangeBranchId("0072");
		input.setPaymentMethod(paymentMethodDTO);
		input.setBank(bank);
		return input;
	}
	private EnterpriseQuotationDTO createInput(){
		EnterpriseQuotationDTO input = new EnterpriseQuotationDTO();
		BankDTO bank = new BankDTO();
		AmountDTO amountDTO = new AmountDTO();
		PaymentMethodDTO paymentMethodDTO = new PaymentMethodDTO();
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

		amountDTO.setAmount(new Double(10000));
		amountDTO.setCurrency("PEN");
		participnt1.setId("P041360");
		IdentityDocumentDTO document = new IdentityDocumentDTO();
		DescriptionDTO documentType = new DescriptionDTO();
		document.setDocumentNumber("73186739");
		documentType.setId("RUC");

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
		monthlyPayrollAmount.setAmount(BigDecimal.valueOf(20000000000000.00).doubleValue());
		employees.setMonthlyPayrollAmount((monthlyPayrollAmount));
		product.setId("842");
		contacto.setContactDetailType("EMAIL");
		contacto.setAddress("marco.yovera@bbva.com");
		contacto1.setContact(contacto);
		contactDetails.add(contacto1);
        input.setInsuredAmount(amountDTO);
		input.setProduct(product);
		input.setParticipants(participantes);
		input.setQuotationReference("2312313");
		input.setEmployees(employees);
		input.setBusinessAgent(busunessAgent);
		input.setContactDetails(contactDetails);
		input.setSaleChannelId("PC");
		input.setUserAudit("zg01293");
		input.setCreationUser("zg01293");
		input.setTraceId("traceId");
		input.setSourceBranchCode("0072");
		input.setLastChangeBranchId("0072");
		input.setPaymentMethod(paymentMethodDTO);
		input.setBank(bank);
		return input;
	}
	private EnterpriseQuotationDTO createInputQuotationReference(){
		EnterpriseQuotationDTO input = createInput();
		input.setQuotationReference("01720842678900");
		return input;
	}
	private EnterpriseQuotationDTO createInputAmountZero(){
		EnterpriseQuotationDTO input = createInput();
		AmountDTO monthlyPayrollAmount = new AmountDTO();
		monthlyPayrollAmount.setCurrency("PEN");
		monthlyPayrollAmount.setAmount(BigDecimal.valueOf(0.00).doubleValue());
		input.getEmployees().setMonthlyPayrollAmount(monthlyPayrollAmount);
		return input;
	}
	private EnterpriseQuotationDTO createInputNumberEmployeesZero(){
		EnterpriseQuotationDTO input = createInput();
		input.getEmployees().setEmployeesNumber(Long.valueOf(0));
		return input;
	}
	private Object createProduct(){
		Map<String, Object> productMap = new HashMap<>();
		productMap.put("nombre", "Juan");
		productMap.put("INSURANCE_PRODUCT_ID", new BigDecimal(842.0));
		productMap.put("INSURANCE_PRODUCT_NAME", "Madrid");
		Object product = productMap;
		return product;

	}
	private List<Map<String, Object>> createPlan(){
		List<Map<String, Object>> listPlans = new ArrayList<>();
		Map<String, Object> mapPlans = new HashMap<>();
		mapPlans.put("INSURANCE_MODALITY_NAME", "PLAN SOLES");
		mapPlans.put("INSURANCE_MODALITY_TYPE", "01");
		mapPlans.put("INSURANCE_COMPANY_MODALITY_ID", "534254");
		listPlans.add(mapPlans);
		return listPlans;

	}
	private Map<String, Object> createPlanSelected(){

		Map<String, Object> mapPlans = new HashMap<>();
		mapPlans.put(ConstantsUtil.InsurancePrdModality.FIELD_INSURANCE_MODALITY_TYPE, "01");
		return mapPlans;

	}
	private List<Map<String, Object>> getPolicy(){
		List<Map<String, Object>> listPlans = new ArrayList<>();
		Map<String, Object> mapPolicy = new HashMap<>();
		mapPolicy.put("POLICY_QUOTA_INTERNAL_ID", "01720842678900");
		Map<String, Object> mapPolicy2 = new HashMap<>();
		mapPolicy2.put("POLICY_QUOTA_INTERNAL_ID", "01720842678901");
		listPlans.add(mapPolicy);
		listPlans.add(mapPolicy2);
		return listPlans;

	}
	private Map<String, Object> getArgumentsPlans(){

		Map<String, Object> arguments = new HashMap<>();
		arguments.put(ConstantsUtil.InsurancePrdModality.FIELD_INSURANCE_PRODUCT_ID, new BigDecimal(842));
		arguments.put(ConstantsUtil.InsurancePrdModality.FIELD_SALE_CHANNEL_ID, "PC");
		return arguments;

	}
	private Map<String, Object> getArgumentsPlanSelected(){

		Map<String, Object> arguments = new HashMap<>();
		arguments.put(ConstantsUtil.QuotationModMap.POLICY_QUOTA_INTERNAL_ID,"01720842678900");
		return arguments;

	}
	private Map<String, Object> getArgumentsPolicy(){

		Map<String, Object> arguments = new HashMap<>();
		arguments.put(ConstantsUtil.QuotationMap.FIELD_RFQ_INTERNAL_ID, "01720842678900");
		arguments.put(ConstantsUtil.QuotationMap.POLICY_QUOTA_INTERNAL_ID, "01720842678900");
		return arguments;

	}
}
