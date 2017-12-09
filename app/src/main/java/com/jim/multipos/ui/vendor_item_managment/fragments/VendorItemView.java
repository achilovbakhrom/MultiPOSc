package com.jim.multipos.ui.vendor_item_managment.fragments;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.vendor_item_managment.model.VendorWithDebt;

import java.util.List;

/**
 * Created by developer on 20.11.2017.
 */

public interface VendorItemView extends BaseView {
    void initRecyclerView(List<VendorWithDebt> vendorWithDebts);
    void initSearchResults(List<VendorWithDebt> vendorWithDebts, String searchText);
    void initDefault(List<VendorWithDebt> vendorWithDebts);
    void notifyList();
    void closeKeyboard();
    void sendDataToConsignment(Long vendorId, int consignment_type);
    void openVendorDetails(Long vendorId);
    void openVendorBillingStory(Long vendorId,Double totalDebt);
    void openVendorConsignmentsStory(Long vendorId);
    void openPaymentDialog(DatabaseManager databaseManager, Vendor vendor);
}
