package com.jim.multipos.ui.customers;

import com.jim.multipos.core.BasePresenterImpl;

import javax.inject.Inject;

public class CustomersActivityPresenterImpl extends BasePresenterImpl<CustomersActivityView> implements CustomersActivityPresenter{

    @Inject
    protected CustomersActivityPresenterImpl(CustomersActivityView customersActivityView) {
        super(customersActivityView);
    }
}
