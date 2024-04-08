package com.bbva.rbvd.lib.r403.transform.bean;
import com.bbva.rbvd.dto.enterpriseinsurance.createquotation.dao.InsuranceModalityDAO;
import com.bbva.rbvd.dto.enterpriseinsurance.utils.ConstantsUtil;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlanBean {
    private PlanBean(){

    }

    public static List<InsuranceModalityDAO> getPlanInformation(List<Map<String,Object>> planMap){

        return planMap.stream()
                .map(plansMap -> {
                    InsuranceModalityDAO planInformationDAO = new InsuranceModalityDAO();
                    planInformationDAO.setInsuranceCompanyModalityId((String) plansMap.get(ConstantsUtil.InsurancePrdModality.FIELD_INSURANCE_COMPANY_MODALITY_ID));
                    planInformationDAO.setInsuranceModalityType((String) plansMap.get(ConstantsUtil.InsurancePrdModality.FIELD_INSURANCE_MODALITY_TYPE));
                    planInformationDAO.setInsuranceModalityName((String) plansMap.get("INSURANCE_MODALITY_NAME"));

                    return planInformationDAO;
                })
                .collect(Collectors.toList());
    }
}
