package com.jim.multipos.data.operations;

import com.jim.multipos.data.db.model.consignment.Consignment;
import com.jim.multipos.data.db.model.consignment.ConsignmentProduct;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Sirojiddin on 10.11.2017.
 */

public interface ConsignmentOperations {
    Observable<Long> insertConsignment(Consignment consignment);
    Observable<Long> insertConsignmentProduct(ConsignmentProduct consignment);
    Observable<List<Consignment>> getConsignments();
    Observable<Boolean> insertConsignmentProducts(List<ConsignmentProduct> consignmentProducts);
}
