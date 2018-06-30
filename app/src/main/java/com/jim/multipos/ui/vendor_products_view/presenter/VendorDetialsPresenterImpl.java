package com.jim.multipos.ui.vendor_products_view.presenter;

import android.os.Bundle;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.ui.vendor_products_view.fragments.VendorDetialsView;
import javax.inject.Inject;

public class VendorDetialsPresenterImpl extends BasePresenterImpl<VendorDetialsView> implements VendorDetialsPresenter {

    @Inject
    public VendorDetialsPresenterImpl(VendorDetialsView view) {
        super(view);
    }

    @Override
    public void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);

    }
}
