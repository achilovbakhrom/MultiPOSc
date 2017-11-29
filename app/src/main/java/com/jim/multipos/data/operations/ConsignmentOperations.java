package com.jim.multipos.data.operations;

import com.jim.multipos.data.db.model.consignment.Consignment;
import com.jim.multipos.data.db.model.consignment.ConsignmentProduct;
import com.jim.multipos.data.db.model.inventory.BillingOperations;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by Sirojiddin on 10.11.2017.
 */

public interface ConsignmentOperations {
    Single<Consignment> insertConsignment(Consignment consignment, BillingOperations operations, List<ConsignmentProduct> consignmentProductList);
    Observable<Long> insertConsignmentProduct(ConsignmentProduct consignment);
    Observable<List<Consignment>> getConsignments();
    Observable<Boolean> insertConsignmentProducts(List<ConsignmentProduct> consignmentProducts);
}
