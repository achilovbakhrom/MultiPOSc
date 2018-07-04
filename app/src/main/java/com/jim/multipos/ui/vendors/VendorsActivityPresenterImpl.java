package com.jim.multipos.ui.vendors;

import com.jim.multipos.core.BasePresenterImpl;

import javax.inject.Inject;

public class VendorsActivityPresenterImpl extends BasePresenterImpl<VendorsActivityView> implements VendorActivityPresenter{

    @Inject
    protected VendorsActivityPresenterImpl(VendorsActivityView vendorsActivityView) {
        super(vendorsActivityView);
    }
}
