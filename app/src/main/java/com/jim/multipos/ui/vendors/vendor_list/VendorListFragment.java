package com.jim.multipos.ui.vendors.vendor_list;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;

import javax.inject.Inject;

import butterknife.BindView;

public class VendorListFragment extends BaseFragment implements VendorListFragmentView {

    @BindView(R.id.rvVendors)
    RecyclerView rvVendors;
    @Inject
    VendorListFragmentPresenter presenter;

    @Override
    protected int getLayout() {
        return R.layout.vendor_list_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        rvVendors.setLayoutManager(new LinearLayoutManager(getContext()));
        presenter.onCreateView(savedInstanceState);
    }
}
