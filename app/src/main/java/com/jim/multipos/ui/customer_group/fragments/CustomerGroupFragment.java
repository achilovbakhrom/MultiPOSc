package com.jim.multipos.ui.customer_group.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.ui.customer_group.adapters.CustomerGroupListAdapter;
import com.jim.multipos.ui.customer_group.adapters.CustomerGroupMembersAdapter;
import com.jim.multipos.ui.customer_group.connector.CustomerGroupConnector;
import com.jim.multipos.ui.customer_group.di.CustomerGroupActivityComponent;
import com.jim.multipos.ui.customer_group.presenters.CustomerGroupFragmentPresenter;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.rxevents.CustomerGroupEvent;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerGroupFragment extends BaseFragment implements CustomerGroupFragmentView, CustomerGroupMembersAdapter.OnItemClickListener {
    @Inject
    RxBusLocal rxBusLocal;
    @Inject
    CustomerGroupFragmentPresenter presenter;
    @BindView(R.id.rvCustomerGroups)
    RecyclerView rvCustomerGroups;
    private Unbinder unbinder;

    public CustomerGroupFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_group_fragment, container, false);

        getComponent(CustomerGroupActivityComponent.class).inject(this);
        presenter.init(this);
        unbinder = ButterKnife.bind(this, view);

        rxBusLocal.send(new CustomerGroupEvent(null, CustomerGroupConnector.CUSTOMER_GROUP_OPENED));

        return view;
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void showCustomers(CustomerGroup customerGroup, List<Customer> customers) {
        rvCustomerGroups.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCustomerGroups.setAdapter(new CustomerGroupMembersAdapter(this, customerGroup, customers));
    }

    @Override
    public void itemClicked(int position, boolean checked) {
        presenter.itemChecked(position, checked);
    }
}
