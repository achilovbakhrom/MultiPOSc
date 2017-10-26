package com.jim.multipos.ui.customer_group.presenters;

import android.util.Log;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.ui.customer_group.connector.AddCustomerGroupConnector;
import com.jim.multipos.ui.customer_group.fragments.CustomerGroupListFragmentView;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.rxevents.CustomerGroupEvent;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

import static com.jim.multipos.ui.customer_group.fragments.AddCustomerGroupFragment.GET_CUSTOMER_GROUP_NAME;
import static com.jim.multipos.ui.customer_group.fragments.CustomerGroupListFragment.CUSTOMER_GROUP_DELETED;
import static com.jim.multipos.ui.customer_group.fragments.CustomerGroupListFragment.CUSTOMER_GROUP_UPDATE;

/**
 * Created by user on 06.09.17.
 */

public class CustomerGroupListFragmentPresenterImpl extends BasePresenterImpl<CustomerGroupListFragmentView> implements CustomerGroupListFragmentPresenter {
    @Inject
    RxBusLocal rxBusLocal;
    private CustomerGroupListFragmentView view;
    //private CustomerGroupOperations operations;
    private List<CustomerGroup> customerGroups;
    /*private RxBusLocal rxBusLocal;
    private RxBus rxBus;*/
    private int prevPosition = 0;
    private int itemSelectedPosition = 0;
    private DatabaseManager databaseManager;

    @Inject
    public CustomerGroupListFragmentPresenterImpl(CustomerGroupListFragmentView view, DatabaseManager databaseManager) {
        super(view);
        this.view = view;
        this.databaseManager = databaseManager;
        /*this.operations = operations;
        this.rxBus = rxBus;
        this.rxBusLocal = rxBusLocal;
        initConnectors(rxBus, rxBusLocal);*/
    }

    @Override
    public void getCustomerGroups() {
         databaseManager.getCustomerGroupOperations().getAllCustomerGroups().subscribe(customerGroups -> {
            this.customerGroups = customerGroups;
            view.showCustomerGroups(customerGroups, itemSelectedPosition);
        });
    }

    @Override
    public void customerGroupAdded() {
        prevPosition = itemSelectedPosition;
        itemSelectedPosition = 0;
        getCustomerGroups();
    }

    @Override
    public void itemClicked(int position) {
        prevPosition = itemSelectedPosition;
        itemSelectedPosition = position;
        /*CustomerGroup customerGroup = customerGroups.get(position);
        rxBusLocal.send(new CustomerGroupEvent(customerGroup, AddCustomerGroupConnector.ITEM_CLICKED));*/

        rxBusLocal.send(new CustomerGroupEvent(null, GET_CUSTOMER_GROUP_NAME));
    }

    @Override
    public void addItemClicked() {
        prevPosition = itemSelectedPosition;
        itemSelectedPosition = 0;

        rxBusLocal.send(new CustomerGroupEvent(null, GET_CUSTOMER_GROUP_NAME));
        //rxBusLocal.send(new CustomerGroupEvent(null, AddCustomerGroupConnector.ADD_CUSTOMER_GROUP_CLICKED));
    }

    @Override
    public void deleteCustomerGroup(CustomerGroup customerGroup) {
        if (customerGroup.getCustomers().isEmpty()) {
            databaseManager.getCustomerGroupOperations().removeCustomerGroup(customerGroup).subscribe(aBoolean -> {
                customerGroups.remove(customerGroup);
                view.updateRV();
                prevPosition = 0;
                itemSelectedPosition = 0;
                rxBusLocal.send(new CustomerGroupEvent(null, CUSTOMER_GROUP_DELETED));
            });
        } else {
            view.showCustomerGroupRemoveWarningDialog();
        }
    }

    @Override
    public void checkCustomerGroupName(String name) {
        if (!name.isEmpty() && !customerGroups.get(prevPosition).getName().equals(name)) {
            view.showCustomerGroupSaveDialog();
        } else {
            if (itemSelectedPosition == 0) {
                rxBusLocal.send(new CustomerGroupEvent(null, AddCustomerGroupConnector.ADD_CUSTOMER_GROUP_CLICKED));
            } else {
                CustomerGroup customerGroup = customerGroups.get(itemSelectedPosition);
                rxBusLocal.send(new CustomerGroupEvent(customerGroup, AddCustomerGroupConnector.ITEM_CLICKED));
            }
        }
    }

    @Override
    public void customerGroupUpdate() {
        rxBusLocal.send(new CustomerGroupEvent(null, CUSTOMER_GROUP_UPDATE));
    }
}
