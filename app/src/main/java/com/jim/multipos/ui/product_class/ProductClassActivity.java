package com.jim.multipos.ui.product_class;

import android.os.Bundle;

import com.jim.mpviews.MpToolbar;
import com.jim.multipos.core.DoubleSideActivity;
import com.jim.multipos.ui.product_class.fragments.AddProductClassFragment;
import com.jim.multipos.ui.product_class.fragments.ProductClassListFragment;
import com.jim.multipos.utils.RxBus;

import javax.inject.Inject;

public class ProductClassActivity extends DoubleSideActivity implements ProductClassView {

    @Inject
    RxBus rxBus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addFragmentToLeft(new AddProductClassFragment());
        addFragmentToRight(new ProductClassListFragment());

    }

    @Override
    protected int getToolbarMode() {
        return MpToolbar.DEFAULT_TYPE;
    }



    @Override
    protected void onDestroy() {

        super.onDestroy();
    }


}
