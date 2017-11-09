package com.jim.multipos.ui.customer_group_new.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerGroupsFragment extends BaseFragment {
    @BindView(R.id.rvCustomerGroups)
    RecyclerView rvCustomerGroups;

    @Override
    protected int getLayout() {
        return R.layout.customer_group_list;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        //((CustomerGroupActivity) getActivity()).getPresenter();
    }
}
