package com.jim.multipos.ui.product_class_new;

import android.os.Bundle;

import com.jim.mpviews.MpToolbar;
import com.jim.multipos.core.SimpleActivity;
import com.jim.multipos.ui.product_class_new.fragments.ProductsClassFragment;

/**
 * Created by developer on 17.10.2017.
 */

public class ProductsClassActivity extends SimpleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addFragment(new ProductsClassFragment());
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
