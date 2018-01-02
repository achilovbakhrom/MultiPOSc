package com.jim.multipos.ui.customer_debt;

import com.jim.multipos.core.BasePresenterImpl;

import javax.inject.Inject;

/**
 * Created by Sirojiddin on 29.12.2017.
 */

public class CustomerDebtActivityPresenterImpl extends BasePresenterImpl<CustomerDebtActivityView> implements CustomerDebtActivityPresenter {

    @Inject
    protected CustomerDebtActivityPresenterImpl(CustomerDebtActivityView customerDebtView) {
        super(customerDebtView);
    }
}
