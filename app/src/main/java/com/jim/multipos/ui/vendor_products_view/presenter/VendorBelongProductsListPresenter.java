package com.jim.multipos.ui.vendor_products_view.presenter;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.vendor_products_view.model.ProductState;

public interface VendorBelongProductsListPresenter extends Presenter{
    void onInvoiceClick(ProductState state);
    void onOutvoiceClick(ProductState state);
    void searchText(String textSearched);
    void updateVendorDetials(Vendor updated);
}
