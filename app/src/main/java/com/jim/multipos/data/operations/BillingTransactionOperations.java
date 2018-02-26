package com.jim.multipos.data.operations;

import com.jim.multipos.data.db.model.inventory.BillingOperations;
import com.jim.multipos.ui.vendor_item_managment.model.VendorWithDebt;

import java.util.Calendar;
import java.util.List;
import java.util.Observable;

import io.reactivex.Single;

/**
 * Created by bakhrom on 10/23/17.
 */

public interface BillingTransactionOperations {
    Single<List<BillingOperations>> getBillingOperationForVendor(Long vendorId);
    Single<List<BillingOperations>> getBillingOperationInteval(Long vendorId, Calendar fromDate,Calendar toDate);
    Single<Double> getVendorDebt(Long vendorId);
    Single<List<BillingOperations>> getBillingOperationsInInterval(Calendar fromDate, Calendar toDate);
    Single<Double> getBillingOperationsAmountInInterval(Long accountId, Calendar fromDate, Calendar toDate);
}
