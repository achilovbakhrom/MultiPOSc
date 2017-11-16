package com.jim.multipos.ui.customer_group_new;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerGroup;

import java.util.List;

/**
 * Created by Portable-Acer on 03.11.2017.
 */

public interface CustomerGroupPresenter extends Presenter {
    List<CustomerGroup> getCustomerGroups();
    CustomerGroup getCustomerGroupById(long id);
    List<Customer> getCustomers();
    void clearAddFragment();
    void showSelectedCustomerGroup(int position);
    void updateCustomerGroup(CustomerGroup customerGroup);
    void addCustomerGroup(String name, boolean isActive);
    void removeCustomerGroup(CustomerGroup customerGroup);
    boolean isCustomerGroupExists(String name);
    void addCustomerToCustomerGroup(CustomerGroup customerGroup, Customer customer);
    void removeCustomerFromCustomerGroup(CustomerGroup customerGroup, Customer customer);
}
