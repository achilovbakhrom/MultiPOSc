package com.jim.multipos.ui.vendors.vendor_edit;

import com.jim.multipos.core.BasePresenterImpl;

import javax.inject.Inject;

public class VendorEditFragmentPresenterImpl extends BasePresenterImpl<VendorEditFragmentView> implements VendorEditFragmentPresenter {

    @Inject
    protected VendorEditFragmentPresenterImpl(VendorEditFragmentView vendorEditFragmentVirw) {
        super(vendorEditFragmentVirw);
    }
}
