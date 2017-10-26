package com.jim.multipos.ui.customer_group.fragments;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerGroup;

import java.util.List;

/**
 * Created by user on 07.09.17.
 */

public interface CustomerGroupFragmentView extends BaseView {
    void showCustomers(CustomerGroup customerGroup, List<Customer> customers);
}
