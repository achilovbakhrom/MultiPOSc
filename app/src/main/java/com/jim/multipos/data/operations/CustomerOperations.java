package com.jim.multipos.data.operations;

import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerGroup;

import java.util.List;

import io.reactivex.Observable;


/**
 * Created by user on 02.09.17.
 */

public interface CustomerOperations {
    Observable<Long> addCustomer(Customer customer);
    Observable<Boolean> addCustomers(List<Customer> customers);
    Observable<Boolean> removeCustomer(Customer customer);
    Observable<Boolean> removeAllCustomers();
    Observable<List<Customer>> getAllCustomers();
    Observable<List<CustomerGroup>> getCustomerGroups(Customer customer);
    Observable<Boolean> isCustomerExists(String name);
}
