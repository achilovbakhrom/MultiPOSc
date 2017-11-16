package com.jim.multipos.data.operations;

import com.jim.multipos.data.db.model.customer.CustomerGroup;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by user on 06.09.17.
 */

public interface CustomerGroupOperations {
    Observable<Long> addCustomerGroup(CustomerGroup customerGroup);
    Observable<Boolean> addCustomerGroups(List<CustomerGroup> customerGroups);
    Observable<Boolean> removeCustomerGroup(CustomerGroup customerGroup);
    Observable<Boolean> removeAllCustomerGroups();
    Observable<List<CustomerGroup>> getAllCustomerGroups();
    Observable<Boolean> isCustomerGroupExists(String name);
    Observable<CustomerGroup> getCustomerGroupByName(String name);
    Observable<CustomerGroup> getCustomerGroupById(long id);
}
