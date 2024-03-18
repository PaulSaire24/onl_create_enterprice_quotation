package com.bbva.rbvd;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.elara.domain.transaction.Context;
import com.bbva.elara.domain.transaction.Severity;
import com.bbva.elara.domain.transaction.TransactionParameter;
import com.bbva.elara.domain.transaction.request.TransactionRequest;
import com.bbva.elara.domain.transaction.request.body.CommonRequestBody;
import com.bbva.elara.domain.transaction.request.header.CommonRequestHeader;
import com.bbva.elara.test.osgi.DummyBundleContext;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.*;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.dto.CreateQuotationDTO;
import com.bbva.rbvd.lib.r403.RBVDR403;

import java.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Test for transaction RBVDT40201PETransaction
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/elara-test.xml",
		"classpath:/META-INF/spring/RBVDT40201PETest.xml" })
public class RBVDT40201PETransactionTest {
	@Resource(name = "rbvdR403")
	private RBVDR403 rbvdR403;
	@Autowired
	private RBVDT40201PETransaction transaction;

	@Resource(name = "dummyBundleContext")
	private DummyBundleContext bundleContext;

	@Mock
	private CommonRequestHeader header;

	@Mock
	private TransactionRequest transactionRequest;



	@Before
	public void initializeClass() throws Exception {
		// Initializing mocks
		MockitoAnnotations.initMocks(this);
		// Start BundleContext
		this.transaction.start(bundleContext);
		// Setting Context
		this.transaction.setContext(new Context());
		// Set Body
		CommonRequestBody commonRequestBody = new CommonRequestBody();
		commonRequestBody.setTransactionParameters(new ArrayList<>());
		this.transactionRequest.setBody(commonRequestBody);
		// Set Header Mock
		this.transactionRequest.setHeader(header);
		// Set TransactionRequest
		this.transaction.getContext().setTransactionRequest(transactionRequest);

	}
	@Test
	public void testNotNull() throws IOException {
	    // Example to Mock the Header
		// Mockito.doReturn("ES").when(header).getHeaderParameter(RequestHeaderParamsName.COUNTRYCODE);


		EnterpriseQuotationDTO response = createInput();
		when(this.rbvdR403.executeCreateQuotation(anyObject())).thenReturn(response);
		Assert.assertNotNull(this.transaction);
		this.transaction.execute();
		assertEquals(Severity.OK, this.transaction.getSeverity());
	}
	@Test
	public void testNull() {

		assertNotNull(this.transaction);

		when(rbvdR403.executeCreateQuotation(anyObject())).thenReturn(null);
		this.transaction.execute();

		assertEquals(Severity.ENR, this.transaction.getSeverity());
	}
	@Test
	public void testThrowBussinessException() {

		assertNotNull(this.transaction);

		when(rbvdR403.executeCreateQuotation(anyObject())).
				thenThrow(new BusinessException("RBVD10094948", false, "ERROR EL TIPO DE DOCUMENTO SOLO PUEDE SER RUC")
					);

		this.transaction.execute();

		assertEquals(Severity.ENR, this.transaction.getSeverity());
	}
	private EnterpriseQuotationDTO createInput(){
		EnterpriseQuotationDTO input = new EnterpriseQuotationDTO();
		PaymentMethodDTO paymentMethodDTO = new PaymentMethodDTO();
		BankDTO bank = new BankDTO();
		ProductDTO product = new ProductDTO();
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
		documentType.setDescription("DNI");
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
        input.setQuotationDate(LocalDate.now());
		input.setPaymentMethod(paymentMethodDTO);
		input.setBank(bank);

		return input;
	}

	// Add Parameter to Transaction
	private void addParameter(final String parameter, final Object value) {
		final TransactionParameter tParameter = new TransactionParameter(parameter, value);
		transaction.getContext().getParameterList().put(parameter, tParameter);
	}

	// Get Parameter from Transaction
	private Object getParameter(final String parameter) {
		final TransactionParameter param = transaction.getContext().getParameterList().get(parameter);
		return param != null ? param.getValue() : null;
	}
}
