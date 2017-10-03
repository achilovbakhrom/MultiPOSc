package com.jim.multipos.ui.customers_edit.entity;

import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerGroup;

import java.util.List;

/**
 * Created by user on 15.09.17.
 */

public class CustomersWithCustomerGroups {
    private Customer customer;
    private List<CustomerGroup> customerGroups;

    public CustomersWithCustomerGroups() {
    }

    public CustomersWithCustomerGroups(Customer customer, List<CustomerGroup> customerGroups) {
        this.customer = customer;
        this.customerGroups = customerGroups;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<CustomerGroup> getCustomerGroups() {
        return customerGroups;
    }

    public void setCustomerGroups(List<CustomerGroup> customerGroups) {
        this.customerGroups = customerGroups;
    }
}
