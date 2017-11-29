package com.jim.multipos.data.operations;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Sirojiddin on 28.11.2017.
 */

public interface PaymentOperations {
    Observable<Long> insertBillingOperation(com.jim.multipos.data.db.model.inventory.BillingOperations billingOperations);
    Observable<List<com.jim.multipos.data.db.model.inventory.BillingOperations>> getBillingOperations();
}
