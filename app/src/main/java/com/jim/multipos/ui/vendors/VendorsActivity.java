package com.jim.multipos.ui.vendors;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jim.mpviews.MpToolbar;
import com.jim.multipos.core.DoubleSideActivity;
import com.jim.multipos.ui.vendors.vendor_edit.VendorEditFragment;
import com.jim.multipos.ui.vendors.vendor_list.VendorListFragment;

public class VendorsActivity extends DoubleSideActivity implements VendorsActivityView{

    @Override
    protected int getToolbarMode() {
        return MpToolbar.DEFAULT_TYPE;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addFragmentToLeft(new VendorEditFragment());
        addFragmentToRight(new VendorListFragment());
    }
}
