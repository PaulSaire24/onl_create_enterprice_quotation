package com.bbva.rbvd.lib.r403.transform.bean;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.dto.ProductDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.rimac.FinancingBO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.rimac.ParticularDataBO;
import com.bbva.rbvd.dto.enterpriseinsurance.commons.rimac.PlanBO;

import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.dto.CreateQuotationDTO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.InsuranceEnterpriseInputBO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.QuotationInputBO;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.rimac.QuotationResponseBO;
import com.bbva.rbvd.lib.r403.service.dao.ProductDAO;
import com.bbva.rbvd.lib.r403.transform.map.ParticipantMap;
import com.bbva.rbvd.lib.r403.utils.ContansUtils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

import static java.util.Objects.nonNull;


public class  QuotationBean {

    private QuotationBean(){}

    public static InsuranceEnterpriseInputBO createQuotationDAO(CreateQuotationDTO quotationCreate,List<Map<String, Object>> planList){
        //los if van en el bussiness
        InsuranceEnterpriseInputBO payload = new InsuranceEnterpriseInputBO();
        QuotationInputBO companyQuotationPayloadBO = new QuotationInputBO();
        ProductDAO productDAO = new ProductDAO();
        ProductDTO productDTO=null;
        if(Objects.nonNull(quotationCreate.getProduct())) {
             productDTO = productDAO.getProductInfo(quotationCreate.getProduct());
        }
        String productName = Objects.nonNull(productDTO) ? productDTO.getName() : "";

        companyQuotationPayloadBO.setDatosParticulares(getDatosParticulares(quotationCreate));
        companyQuotationPayloadBO.setProducto(productName);

        if (isFirstCall(quotationCreate)) {
            companyQuotationPayloadBO.setPlanes(new ArrayList<>());
            companyQuotationPayloadBO.setTipoCotizacion("R");
        } else {
            companyQuotationPayloadBO.setPlanes(ParticipantMap.mapPlan(planList));
            companyQuotationPayloadBO.setTipoCotizacion("C");
        }

        String currency = (Objects.nonNull(quotationCreate) && Objects.nonNull(quotationCreate.getEmployees())
                && Objects.nonNull(quotationCreate.getEmployees().getMonthlyPayrollAmount())
                && Objects.nonNull(quotationCreate.getEmployees().getMonthlyPayrollAmount().getCurrency()))
                ? quotationCreate.getEmployees().getMonthlyPayrollAmount().getCurrency()
                : "";
        companyQuotationPayloadBO.setMoneda(currency);
        payload.setPayload(companyQuotationPayloadBO);
        return payload;
    }
    public static List<ParticularDataBO> getDatosParticulares(CreateQuotationDTO quotationCreate){
        String majorityAge = (nonNull(quotationCreate) && nonNull(quotationCreate.getEmployees()) && quotationCreate.getEmployees().getAreMajorityAge()) ? "S" : "N";
//parte del bussines los datos particulares
        List<ParticularDataBO> particularData = new ArrayList<>();

        ParticularDataBO numeroTrabajadores = new ParticularDataBO();
        numeroTrabajadores.setEtiqueta(ContansUtils.rimacInput.ETIQUETA_1);
        numeroTrabajadores.setCodigo("");
        numeroTrabajadores.setValor(quotationCreate != null && nonNull(quotationCreate.getEmployees()) ? getNumeroTrabajadores(quotationCreate.getEmployees().getEmployeesNumber()) : "");
        particularData.add(numeroTrabajadores);

        ParticularDataBO indicadorEdadTrabajadores = new ParticularDataBO();
        indicadorEdadTrabajadores.setEtiqueta(ContansUtils.rimacInput.ETIQUETA_2);
        indicadorEdadTrabajadores.setValor(majorityAge);
        indicadorEdadTrabajadores.setCodigo("");
        particularData.add(indicadorEdadTrabajadores);

        ParticularDataBO planillaBrutaMensual = new ParticularDataBO();
        planillaBrutaMensual.setEtiqueta(ContansUtils.rimacInput.ETIQUETA_3);
        planillaBrutaMensual.setCodigo("");
        planillaBrutaMensual.setValor(quotationCreate != null && nonNull(quotationCreate.getEmployees()) ? String.valueOf(quotationCreate.getEmployees().getMonthlyPayrollAmount().getAmount()) : "");
        particularData.add(planillaBrutaMensual);

        ParticularDataBO sumaAsegurada = new ParticularDataBO();
        sumaAsegurada.setValor(String.valueOf(10000));
        sumaAsegurada.setEtiqueta(ContansUtils.rimacInput.ETIQUETA_4);
        sumaAsegurada.setCodigo("");
        particularData.add(sumaAsegurada);

        return particularData;
    }


 public static boolean isFirstCall(CreateQuotationDTO input) {
     String quotationReference = input.getQuotationReference();
     return Objects.isNull(quotationReference);
 }
public static String getNumeroTrabajadores(Long numero){

    Integer numeroa =numero.intValue();
    Integer respuesta = (numeroa - 1) / 50 + 1;
        return respuesta.toString();
}
}
