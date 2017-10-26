package com.jim.multipos.data.operations;


import com.jim.multipos.data.db.model.customer.JoinCustomerGroupsWithCustomers;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.internal.operators.observable.ObservableSerialized;

/**
 * Created by user on 12.09.17.
 */

public interface JoinCustomerGroupWithCustomerOperations {
    Observable<Long> addJoinCustomerGroupWithCustomer(JoinCustomerGroupsWithCustomers joinCustomerGroupWithCustomer);
    Observable<Boolean> addJoinCustomerGroupWithCustomers(List<JoinCustomerGroupsWithCustomers> joinCustomerGroupsWithCustomers);
    Observable<Boolean> removeJoinCustomerGroupWithCustomer(Long customerGroupId, Long customerId);
    Observable<Boolean> removeJoinCustomerGroupWithCustomer(Long customerId);
    Observable<Boolean> removeAllJoinCustomerGroupWithCustomer();
    Observable<List<JoinCustomerGroupsWithCustomers>> getAllJoinCustomerGroupsWithCustomers();
}
