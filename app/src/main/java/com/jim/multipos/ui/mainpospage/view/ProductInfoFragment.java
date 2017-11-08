package com.jim.multipos.ui.mainpospage.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;

/**
 * Created by developer on 24.08.2017.
 */

public class ProductInfoFragment extends BaseFragment implements ProductInfoView{



    @Override
    protected int getLayout() {
        return R.layout.product_info_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

}
