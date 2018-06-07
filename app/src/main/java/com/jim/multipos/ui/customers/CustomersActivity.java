package com.jim.multipos.ui.customers;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jim.mpviews.MpToolbar;
import com.jim.multipos.core.SimpleActivity;
import com.jim.multipos.ui.customers.customer.CustomerFragment;

public class CustomersActivity extends SimpleActivity implements CustomersActivityView{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addFragment(new CustomerFragment());
    }

    @Override
    protected int getToolbar() {
        return WITH_TOOLBAR;
    }

    @Override
    protected int getToolbarMode() {
        return MpToolbar.DEFAULT_TYPE;
    }
}
