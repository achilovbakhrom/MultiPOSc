package com.jim.multipos.ui.product_last.fragment;

import android.os.Bundle;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;

/**
 * Created by Achilov Bakhrom on 10/26/17.
 */

public class ProductAddEditFragment extends BaseFragment {

    @Override
    protected int getLayout() {
        return R.layout.add_product_fragment;
    }

    @Override
    protected boolean isAndroidInjectionEnabled() {
        return false;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }
}
