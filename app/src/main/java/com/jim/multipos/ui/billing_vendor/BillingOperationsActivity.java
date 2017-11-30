package com.jim.multipos.ui.billing_vendor;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import com.jim.mpviews.MpToolbar;
import com.jim.multipos.core.SimpleActivity;
import com.jim.multipos.ui.billing_vendor.fragments.BillingOperationFragment;

/**
 * Created by developer on 29.11.2017.
 */

public class BillingOperationsActivity extends SimpleActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BillingOperationFragment billingOperationFragment = new BillingOperationFragment();
        addFragment(billingOperationFragment);
    }


    @Override
    protected int getToolbar() {
        return WITH_TOOLBAR;
    }

    @Override
    protected int getToolbarMode() {
        return MpToolbar.WITH_CALENDAR_TYPE;
    }
}
