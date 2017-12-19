package com.jim.multipos.ui.billing_vendor.fragments;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.inventory.BillingOperations;
import com.jim.multipos.data.db.model.products.Vendor;

import java.util.List;

/**
 * Created by developer on 30.11.2017.
 */

public interface BillingOperationView extends BaseView {
    void setVendorData(Vendor vendor);
    void setTransactions(List<BillingOperations> transactions);
    void notifyListChange(List<BillingOperations> billingOperations);
    void notifyList();
    void updateDebt(Double debt);
    void notifyChange();
    void sendEvent();
}
