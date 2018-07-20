package com.jim.multipos.data.operations;

import io.reactivex.Observable;

/**
 * Created by user on 12.09.17.
 */

public interface JoinCustomerGroupWithCustomerOperations {
    Observable<Long> addCustomerToCustomerGroup(Long customerGroupId, Long customerId);

    Observable<Boolean> removeCustomerFromCustomerGroup(Long customerGroupId, Long customerId);

    Observable<Boolean> removeJoinCustomerGroupWithCustomer(Long customerId);
}
