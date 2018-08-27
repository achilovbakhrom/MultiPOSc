package com.jim.multipos.ui.admin_main_page.fragments.product_class;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;

import butterknife.BindView;

public class ProductClassFragment extends BaseFragment {

    @BindView(R.id.rvProductClass)
    RecyclerView rvProductClass;

    @Override
    protected int getLayout() {
        return R.layout.admin_product_class_layout;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }
}
