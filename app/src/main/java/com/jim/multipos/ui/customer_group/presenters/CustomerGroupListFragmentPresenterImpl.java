package com.jim.multipos.ui.customer_group.presenters;

import android.content.Context;

import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.data.operations.CustomerGroupOperations;
import com.jim.multipos.ui.customer_group.connector.AddCustomerGroupConnector;
import com.jim.multipos.ui.customer_group.connector.CustomerGroupListConnector;
import com.jim.multipos.ui.customer_group.fragments.CustomerGroupListFragmentView;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.rxevents.CustomerGroupEvent;

import java.util.List;

/**
 * Created by user on 06.09.17.
 */

public class CustomerGroupListFragmentPresenterImpl extends CustomerGroupListConnector implements CustomerGroupListFragmentPresenter {
    private CustomerGroupListFragmentView view;
    private CustomerGroupOperations operations;
    private List<CustomerGroup> customerGroups;
    private RxBusLocal rxBusLocal;
    private RxBus rxBus;
    private int itemSelectedPosition = 0;

    public CustomerGroupListFragmentPresenterImpl(CustomerGroupOperations operations, RxBus rxBus, RxBusLocal rxBusLocal) {
        this.operations = operations;
        this.rxBus = rxBus;
        this.rxBusLocal = rxBusLocal;
    }

    @Override
    public void init(CustomerGroupListFragmentView view) {
        this.view = view;
        initConnectors(rxBus, rxBusLocal);
    }

    @Override
    public void getCustomerGroups() {
        operations.getAllCustomerGroups().subscribe(customerGroups -> {
            this.customerGroups = customerGroups;
            view.showCustomerGroups(customerGroups, itemSelectedPosition);
        });
    }

    @Override
    public void customerGroupAdded() {
        getCustomerGroups();
    }

    @Override
    public void itemClicked(int position) {
        itemSelectedPosition = position;
        CustomerGroup customerGroup = customerGroups.get(position);
        rxBusLocal.send(new CustomerGroupEvent(customerGroup, AddCustomerGroupConnector.ITEM_CLICKED));
    }

    @Override
    public void addItemClicked() {
        itemSelectedPosition = 0;
        rxBusLocal.send(new CustomerGroupEvent(null, AddCustomerGroupConnector.ADD_CUSTOMER_GROUP_CLICKED));
    }
}
