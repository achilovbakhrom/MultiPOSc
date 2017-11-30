package com.jim.multipos.ui.billing_vendor.presenter;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.ui.billing_vendor.fragments.BillingOperationView;

import javax.inject.Inject;

/**
 * Created by developer on 30.11.2017.
 */

public class BillingOperationPresenterImpl extends BasePresenterImpl<BillingOperationView> implements BillingOperationPresenter {
    @Inject
    protected BillingOperationPresenterImpl(BillingOperationView billingOperationView) {
        super(billingOperationView);
    }
}
