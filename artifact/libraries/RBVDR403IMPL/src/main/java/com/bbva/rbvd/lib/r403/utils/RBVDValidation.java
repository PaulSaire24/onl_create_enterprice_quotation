package com.bbva.rbvd.lib.r403.utils;

import com.bbva.apx.exception.business.BusinessException;

public class RBVDValidation {
    private RBVDValidation() {
    }

    public static BusinessException build(RBVDErrors error) {
        return new BusinessException(error.getAdviceCode(), error.isRollback(), error.getMessage());
    }
}
