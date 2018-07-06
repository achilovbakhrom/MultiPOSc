package com.jim.multipos.ui.vendors.vendor_list;

import android.os.Bundle;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.products.Vendor;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class VendorListFragmentPresenterImpl extends BasePresenterImpl<VendorListFragmentView> implements VendorListFragmentPresenter {

    private DatabaseManager databaseManager;
    private List<Vendor> vendorList;


    @Inject
    protected VendorListFragmentPresenterImpl(VendorListFragmentView view, DatabaseManager databaseManager) {
        super(view);
        vendorList = new ArrayList<>();
        this.databaseManager = databaseManager;
    }

    @Override
    public void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);
        vendorList = databaseManager.getVendors().blockingSingle();
        vendorList.add(0, null);
        view.setVendorsList(vendorList);
    }

    @Override
    public void refreshList() {
        vendorList = databaseManager.getVendors().blockingSingle();
        vendorList.add(0, null);
        view.setVendorsList(vendorList);
    }
}
