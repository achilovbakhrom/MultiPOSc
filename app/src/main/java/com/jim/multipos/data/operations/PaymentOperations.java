package com.jim.multipos.data.operations;

import com.jim.multipos.data.db.model.inventory.BillingOperations;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by Sirojiddin on 28.11.2017.
 */

public interface PaymentOperations {
    Single<BillingOperations> insertBillingOperation(BillingOperations billingOperations);
    Observable<List<BillingOperations>> getBillingOperations();
    Single<BillingOperations> getBillingOperationsByConsignmentId(Long consignmentId);
    Single<BillingOperations> getBillingOperationsById(Long firstPayId);
    Single<List<BillingOperations>> getBillingOperationByRootId(Long rootId);
}
