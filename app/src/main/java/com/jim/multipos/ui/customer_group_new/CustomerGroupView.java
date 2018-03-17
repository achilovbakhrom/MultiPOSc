package com.jim.multipos.ui.customer_group_new;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerGroup;

/**
 * Created by Portable-Acer on 03.11.2017.
 */

public interface CustomerGroupView extends BaseView {
    void clearAddFragment();
    void showSelectedCustomerGroup(CustomerGroup customerGroup);
    void addItem(CustomerGroup customerGroup);
    void removeItem(CustomerGroup customerGroup);
    void updateItem(CustomerGroup customerGroup);
    void showCustomersFragment(long customerGroupId);
    void addedCustomerToCustomerGroup(Customer customer);
    void removedCustomerFromCustomerGroup(Customer customer);
    void setDefaultValueCustomerGroupRV();
    void showActiveCustomerGroupWarningDialog();
    String getCustomerGroupName();
}
