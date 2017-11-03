package com.jim.multipos.ui.discount;

import android.os.Bundle;

import com.jim.mpviews.MpToolbar;
import com.jim.multipos.core.SimpleActivity;
import com.jim.multipos.ui.discount.fragments.DiscountAddingFragment;

public class DiscountAddingActivity extends SimpleActivity {
    DiscountAddingFragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragment = new DiscountAddingFragment();
        addFragment(fragment);
    }

    @Override
    protected int getToolbar() {
        return WITH_TOOLBAR;
    }

    @Override
    protected int getToolbarMode() {
        return MpToolbar.DEFAULT_TYPE;
    }

    @Override
    public void onBackPressed() {
        fragment.closeDiscountActivity();

    }
}
