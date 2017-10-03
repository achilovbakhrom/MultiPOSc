package com.jim.multipos.ui.customer_group.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.ui.customer_group.adapters.CustomerGroupListAdapter;
import com.jim.multipos.ui.customer_group.di.CustomerGroupActivityComponent;
import com.jim.multipos.ui.customer_group.presenters.CustomerGroupListFragmentPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerGroupListFragment extends BaseFragment implements CustomerGroupListFragmentView, CustomerGroupListAdapter.OnItemClickListener {
    @Inject
    CustomerGroupListFragmentPresenter presenter;
    @BindView(R.id.rvCustomerGroups)
    RecyclerView rvCustomerGroups;
    private Unbinder unbinder;

    public CustomerGroupListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_group_list, container, false);

        getComponent(CustomerGroupActivityComponent.class).inject(this);
        presenter.init(this);
        unbinder = ButterKnife.bind(this, view);

        presenter.getCustomerGroups();

        return view;
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();

        super.onDestroyView();
    }

    @Override
    public void showCustomerGroups(List<CustomerGroup> customerGroups, int selectedPosition) {
        rvCustomerGroups.setItemAnimator(null);
        rvCustomerGroups.setLayoutManager(new GridLayoutManager(getContext(), 6));
        rvCustomerGroups.setAdapter(new CustomerGroupListAdapter(customerGroups, this, selectedPosition));
    }

    @Override
    public void onAddButtonPressed() {
        presenter.addItemClicked();
    }

    @Override
    public void onItemPressed(int t) {
        presenter.itemClicked(t);
    }
}
