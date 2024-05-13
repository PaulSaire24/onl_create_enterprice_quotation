package com.bbva.rbvd.lib.r403.pattern.impl;

import com.bbva.rbvd.lib.r403.pattern.CreateQuotation;
import com.bbva.rbvd.lib.r403.pattern.PostCreateQuotation;
import com.bbva.rbvd.lib.r403.pattern.PreCreateQuotation;

public abstract class CreateQuotationDecorator implements CreateQuotation {
    private PreCreateQuotation preCreateQuotation;
    private PostCreateQuotation postCreateQuotation;
    public CreateQuotationDecorator(PreCreateQuotation preCreateQuotation, PostCreateQuotation postCreateQuotation) {
        this.preCreateQuotation = preCreateQuotation;
        this.postCreateQuotation = postCreateQuotation;
    }

    public PreCreateQuotation getPreCreateQuotation() {
        return preCreateQuotation;
    }

    public PostCreateQuotation getPostCreateQuotation() {
        return postCreateQuotation;
    }
}
