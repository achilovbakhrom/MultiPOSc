package com.jim.multipos.data.operations;

import com.jim.multipos.data.db.model.inventory.BillingOperations;
import com.jim.multipos.ui.vendor_item_managment.model.VendorWithDebt;

import java.util.List;
import java.util.Observable;

import io.reactivex.Single;

/**
 * Created by bakhrom on 10/23/17.
 */

public interface BillingTransactionOperations {
    Single<List<BillingOperations>> getBillingOperationForVendor(Long vendorId);
    Single<Double> getVendorDebt(Long vendorId);

}
