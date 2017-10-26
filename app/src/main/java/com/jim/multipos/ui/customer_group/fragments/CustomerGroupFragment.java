package com.jim.multipos.ui.customer_group.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.ui.customer_group.adapters.CustomerGroupMembersAdapter;
import com.jim.multipos.ui.customer_group.connector.CustomerGroupConnector;
import com.jim.multipos.ui.customer_group.presenters.CustomerGroupFragmentPresenter;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.rxevents.CustomerGroupEvent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerGroupFragment extends BaseFragment implements CustomerGroupFragmentView, CustomerGroupMembersAdapter.OnItemClickListener {
    public static final String SHOW_CUSTOMERS = "show_customers";
    public static final String CUSTOMER_GROUP_OPENED = "customer_group_opened";
    public static final String CUSTOMER_GROUP_ADDED = "customer_group_added";

    @Inject
    RxBusLocal rxBusLocal;
    @Inject
    CustomerGroupFragmentPresenter presenter;
    @BindView(R.id.rvCustomerGroups)
    RecyclerView rvCustomerGroups;
    private ArrayList<Disposable> subscriptions = new ArrayList<>();

    public void showCustomers(CustomerGroup customerGroup, List<Customer> customers) {
        rvCustomerGroups.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCustomerGroups.setAdapter(new CustomerGroupMembersAdapter(this, customerGroup, customers));
    }

    public void itemClicked(int position, boolean checked) {
        presenter.itemChecked(position, checked);
    }

    @Override
    protected int getLayout() {
        return R.layout.customer_group_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        rxBusLocal.send(new CustomerGroupEvent(null, CustomerGroupConnector.CUSTOMER_GROUP_OPENED));
    }

    @Override
    protected void rxConnections() {
        subscriptions.add(rxBusLocal.toObservable().subscribe(o -> {
            if (o instanceof CustomerGroupEvent) {
                CustomerGroupEvent customerGroupEvent = (CustomerGroupEvent) o;
                if (customerGroupEvent.getEventType().equals(SHOW_CUSTOMERS)) {
                    presenter.showCustomers(customerGroupEvent.getCustomerGroup());
                } else if (customerGroupEvent.getEventType().equals(CUSTOMER_GROUP_ADDED)) {
                    presenter.customerGroupAdded(customerGroupEvent.getCustomerGroup());
                }
            }
        }));
    }

    @Override
    public void onDestroy() {
        RxBus.removeListners(subscriptions);
        super.onDestroy();
    }
}
