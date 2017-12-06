package com.jim.multipos.ui.vendor_products_view;

import com.jim.multipos.core.BaseView;

/**
 * Created by Portable-Acer on 17.11.2017.
 */

public interface VendorProductsView extends BaseView {
    void sendDataToConsignment(int consignmentType, long vendorId);
    void sendDataToConsignmentList(long vendorId);
    void openIncomeConsignmentToProduct(int incomeConsignment, long vendorId, Long productId);
    void openVendorEditing(long vendorId);
}
