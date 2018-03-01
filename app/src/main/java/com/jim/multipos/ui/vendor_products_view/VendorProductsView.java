package com.jim.multipos.ui.vendor_products_view;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Contact;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.vendor_products_view.model.ProductState;

import java.util.List;

/**
 * Created by Portable-Acer on 17.11.2017.
 */

public interface VendorProductsView extends BaseView {
    void sendDataToConsignment(int consignmentType, long vendorId);
    void sendDataToConsignmentList(long vendorId);
    void openIncomeConsignmentToProduct(int incomeConsignment, long vendorId, Long productId);
    void openVendorEditing(long vendorId);
    void updateAdapterItems(List<ProductState> inventoryStates);
    void initVendorDetails(String name, String photoPath, String address, String contactName, List<Contact> contacts, Double debt, double paid, String abbr);
    void openPaymentsList(long vendorId, Double debt);
    void openPayDialog(Vendor vendor, DatabaseManager databaseManager);
    void updateVendorBillings(Double debt, double paid, String abbr);
    void sendEvent(int event);
}
