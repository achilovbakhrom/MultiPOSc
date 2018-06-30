package com.jim.multipos.ui.vendor_products_view.presenter;

import android.os.Bundle;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.vendor_products_view.fragments.VendorBelongProductsListView;
import com.jim.multipos.ui.vendor_products_view.model.ProductState;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.jim.multipos.ui.inventory.InventoryActivity.VENDOR_ID;

public class VendorBelongProductsListPresenterImpl extends BasePresenterImpl<VendorBelongProductsListView> implements VendorBelongProductsListPresenter{

    private DatabaseManager databaseManager;
    List<ProductState> items;
    Vendor vendor;
    @Inject
    public VendorBelongProductsListPresenterImpl(VendorBelongProductsListView view, DatabaseManager databaseManager) {
        super(view);
        this.databaseManager = databaseManager;
        items = new ArrayList<>();
    }

    @Override
    public void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);
        if(bundle!=null) {
            databaseManager.getVendorById(bundle.getLong(VENDOR_ID)).subscribe(vendor1 -> {
                vendor = vendor1;
                databaseManager.getVendorProductsWithStates(vendor.getId()).subscribe(productStates -> {
                    items = productStates;
                    view.initRecyclerView(items);
                });
            });

        }
    }

    @Override
    public void onInvoiceClick(ProductState state) {
        view.openVendorInvoiceWithProduct(vendor,state.getProduct());
    }

    @Override
    public void onOutvoiceClick(ProductState state) {
        view.openVendorOutvoiceWithProduct(vendor,state.getProduct());
    }
}
