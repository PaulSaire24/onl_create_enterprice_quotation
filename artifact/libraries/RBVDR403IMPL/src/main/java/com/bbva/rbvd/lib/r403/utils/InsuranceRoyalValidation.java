package com.bbva.rbvd.lib.r403.utils;

import com.bbva.apx.exception.business.BusinessException;

public class InsuranceRoyalValidation {
        private InsuranceRoyalValidation() {
        }

        public static BusinessException build(InsuranceRoyalErrors error) {
            return new BusinessException(error.getAdviceCode(), error.isRollback(), error.getMessage());
        }
}
