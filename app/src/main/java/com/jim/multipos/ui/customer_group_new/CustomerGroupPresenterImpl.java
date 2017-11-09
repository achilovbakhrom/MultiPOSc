package com.jim.multipos.ui.customer_group_new;

import com.jim.multipos.core.BasePresenterImpl;

import javax.inject.Inject;

/**
 * Created by Portable-Acer on 03.11.2017.
 */

public class CustomerGroupPresenterImpl extends BasePresenterImpl<CustomerGroupView> implements CustomerGroupPresenter {
    @Inject
    public CustomerGroupPresenterImpl(CustomerGroupView customerGroupView) {
        super(customerGroupView);
    }

    @Override
    public void getCustomerGroups() {

    }
}
