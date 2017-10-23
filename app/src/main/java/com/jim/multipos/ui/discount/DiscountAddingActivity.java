package com.jim.multipos.ui.discount;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jim.mpviews.MpToolbar;
import com.jim.multipos.R;
import com.jim.multipos.core.SimpleActivity;
import com.jim.multipos.ui.discount.fragments.DiscountAddingFragment;

public class DiscountAddingActivity extends SimpleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addFragment(new DiscountAddingFragment());
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
