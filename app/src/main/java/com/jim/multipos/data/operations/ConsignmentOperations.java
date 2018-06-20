package com.jim.multipos.data.operations;

import com.jim.multipos.data.db.model.consignment.Consignment;
import com.jim.multipos.data.db.model.consignment.ConsignmentProduct;
import com.jim.multipos.data.db.model.inventory.BillingOperations;

import java.util.Calendar;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by Sirojiddin on 10.11.2017.
 */

public interface ConsignmentOperations {
//    Single<Consignment> insertConsignment(Consignment consignment, List<BillingOperations> operations, List<ConsignmentProduct> consignmentProductList, List<WarehouseOperations> warehouseOperationsList);
    Observable<Long> insertConsignmentProduct(ConsignmentProduct consignment);
    Observable<List<Consignment>> getConsignments();
    Single<List<Consignment>> getConsignmentsByVendorId(Long vendorId);
    Observable<Boolean> insertConsignmentProducts(List<ConsignmentProduct> consignmentProducts);
    Single<List<ConsignmentProduct>> getConsignmentProductsByConsignmentId(Long consignmentId);
    Single<Consignment> getConsignmentById(Long consignmentId);
    Single<List<Consignment>> getConsignmentsInIntervalByVendor(Long vendorId, Calendar fromDate, Calendar toDate);
    Single<Boolean> isInvoiceNumberExists(String number);
    Single<Long> getConsignmentByWarehouseId(Long warehouseId);
    Single<List<Consignment>> getConsignmentsInInterval(Calendar fromDate, Calendar toDate);
    Single<List<ConsignmentProduct>> getConsignmentProductsInterval(Calendar fromDate, Calendar toDate);
}
