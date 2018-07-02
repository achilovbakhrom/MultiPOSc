package com.jim.multipos.ui.vendor_products_view.fragments;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.vendor_products_view.model.ProductState;

import java.util.List;

public interface VendorBelongProductsListView extends BaseView {
    void initRecyclerView(List<ProductState> productStates);
    void updateProductStates(List<ProductState> productStates);
    void initSearchResults(List<ProductState> productStates, String searchText);
    void openVendorInvoiceWithProduct(Vendor vendor, Product product);
    void openVendorOutvoiceWithProduct(Vendor vendor, Product product);
}
