package com.jim.multipos.ui.billing_vendor.fragments;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;

import butterknife.BindView;

/**
 * Created by developer on 30.11.2017.
 */

public class BillingOperationFragment extends BaseFragment implements BillingOperationView{
    @BindView(R.id.rvBilling)
    RecyclerView rvBilling;

    @Override
    protected int getLayout() {
        return R.layout.billing_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Log.d("FRGINIT", "init: ");
    }
}
