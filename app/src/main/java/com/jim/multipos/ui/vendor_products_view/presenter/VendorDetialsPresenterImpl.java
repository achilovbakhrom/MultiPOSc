package com.jim.multipos.ui.vendor_products_view.presenter;

import android.os.Bundle;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.vendor_products_view.fragments.VendorDetialsView;
import javax.inject.Inject;

import static com.jim.multipos.ui.inventory.InventoryActivity.VENDOR_ID;

public class VendorDetialsPresenterImpl extends BasePresenterImpl<VendorDetialsView> implements VendorDetialsPresenter {

    private DatabaseManager databaseManager;
    private Vendor vendor;
    @Inject
    public VendorDetialsPresenterImpl(VendorDetialsView view, DatabaseManager databaseManager) {
        super(view);
        this.databaseManager = databaseManager;
    }

    @Override
    public void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);
        if(bundle!=null) {
            databaseManager.getVendorById(bundle.getLong(VENDOR_ID)).subscribe(vendor1 -> {
                vendor = vendor1;
                databaseManager.getVendorDebt(vendor.getId()).subscribe(aDouble -> {
                   view.updateView(vendor,aDouble);
                });
            });
        }
        }

    @Override
    public void updateMyDebtState() {
        databaseManager.getVendorDebt(vendor.getId()).subscribe(aDouble -> {
            view.updateDebt(aDouble);
        });
    }

    @Override
    public void updateVendorDetials(Vendor updated) {
        if(vendor.getId().equals(updated.getId())){
            databaseManager.getVendorDebt(vendor.getId()).subscribe(aDouble -> {
                view.updateDebt(aDouble);
            });
        }
    }
}
