package com.jim.multipos.ui.customer_group.presenters;

import android.util.Log;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.data.db.model.customer.JoinCustomerGroupsWithCustomers;
import com.jim.multipos.ui.customer_group.fragments.CustomerGroupFragmentView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by user on 07.09.17.
 */

public class CustomerGroupFragmentPresenterImpl extends BasePresenterImpl<CustomerGroupFragmentView> implements CustomerGroupFragmentPresenter {
    private CustomerGroupFragmentView view;
    /*private RxBus rxBus;
    private RxBusLocal rxBusLocal;*/
    private CustomerGroup customerGroup;
    private List<Customer> customers;
    /*private CustomerOperations customerOperations;
    private JoinCustomerGroupWithCustomerOperations joinCustomerGroupWithCustomerOperations;*/
    private List<Customer> reverseList;
    private DatabaseManager databaseManager;

    @Inject
    public CustomerGroupFragmentPresenterImpl(CustomerGroupFragmentView view, DatabaseManager databaseManager) {
        /*this.customerOperations = customerOperations;
        this.joinCustomerGroupWithCustomerOperations = joinCustomerGroupWithCustomerOperations;
        this.rxBus = rxBus;
        this.rxBusLocal = rxBusLocal;*/
        super(view);
        this.view = view;
        this.databaseManager = databaseManager;
        reverseList = new ArrayList<>();
        //initConnectors(rxBus, rxBusLocal);
    }

    @Override
    public void itemChecked(int position, boolean checked) {
        if (checked) {
            if (customerGroup.getName() == null) {
                reverseList.add(customers.get(position));
            } else {
                JoinCustomerGroupsWithCustomers joinCustomerGroupsWithCustomers = new JoinCustomerGroupsWithCustomers();
                joinCustomerGroupsWithCustomers.setCustomerGroupId(customerGroup.getId());
                joinCustomerGroupsWithCustomers.setCustomerId(customers.get(position).getId());

                /*databaseManager.getJoinCustomerGroupWithCustomerOperations().addJoinCustomerGroupWithCustomer(joinCustomerGroupsWithCustomers).subscribe(aLong -> {
                    customerGroup.getCustomers().add(customers.get(position));
                });*/
            }
        } else {
            if (customerGroup.getName() == null) {
                reverseList.remove(customers.get(position));
            } else {
                //TODO IT AFTER ENTITY FIX
                Long customerGroupId = customerGroup.getId();
                Long customerId = customers.get(position).getId();

                /*databaseManager.getJoinCustomerGroupWithCustomerOperations().removeJoinCustomerGroupWithCustomer(customerGroupId, customerId).subscribe(aBoolean -> {
                    customerGroup.getCustomers().remove(customers.get(position));
                });*/
            }
        }
    }

    @Override
    public void customerGroupAdded(CustomerGroup customerGroup) {
        if (reverseList != null && (!reverseList.isEmpty())) {
            this.customerGroup = customerGroup;

            for (Customer c : reverseList) {
                JoinCustomerGroupsWithCustomers joinCustomerGroupsWithCustomers = new JoinCustomerGroupsWithCustomers();
                joinCustomerGroupsWithCustomers.setCustomerGroupId(customerGroup.getId());
                joinCustomerGroupsWithCustomers.setCustomerId(c.getId());

                /*databaseManager.getJoinCustomerGroupWithCustomerOperations().addJoinCustomerGroupWithCustomer(joinCustomerGroupsWithCustomers).subscribe(aLong -> {
                    customerGroup.getCustomers().add(c);

                    if (view != null) {
                        view.showCustomers(customerGroup, customers);
                    }
                });*/
            }

            reverseList = null;
        }
    }

    @Override
    public void showCustomers(CustomerGroup customerGroup) {
        databaseManager.getCustomerOperations().getAllCustomers().subscribe(customers -> {
            this.customers = customers;

            if (customerGroup == null) {
                this.customerGroup = new CustomerGroup();
            } else {
                this.customerGroup = customerGroup;
            }

            view.showCustomers(this.customerGroup, customers);
        });
    }
}
