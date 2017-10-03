package com.jim.multipos.ui.customer_group.presenters;

import android.content.Context;
import android.util.Log;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.data.db.model.customer.JoinCustomerGroupsWithCustomers;
import com.jim.multipos.data.operations.CustomerGroupOperations;
import com.jim.multipos.data.operations.CustomerOperations;
import com.jim.multipos.data.operations.JoinCustomerGroupWithCustomerOperations;
import com.jim.multipos.ui.customer_group.connector.CustomerGroupConnector;
import com.jim.multipos.ui.customer_group.fragments.CustomerGroupFragmentView;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 07.09.17.
 */

public class CustomerGroupFragmentPresenterImpl extends CustomerGroupConnector implements CustomerGroupFragmentPresenter {
    private CustomerGroupFragmentView view;
    private RxBus rxBus;
    private RxBusLocal rxBusLocal;
    private CustomerGroup customerGroup;
    private List<Customer> customers;
    private CustomerOperations customerOperations;
    private JoinCustomerGroupWithCustomerOperations joinCustomerGroupWithCustomerOperations;
    private List<Customer> reverseList;

    public CustomerGroupFragmentPresenterImpl(CustomerOperations customerOperations, JoinCustomerGroupWithCustomerOperations joinCustomerGroupWithCustomerOperations, RxBus rxBus, RxBusLocal rxBusLocal) {
        this.customerOperations = customerOperations;
        this.joinCustomerGroupWithCustomerOperations = joinCustomerGroupWithCustomerOperations;
        this.rxBus = rxBus;
        this.rxBusLocal = rxBusLocal;
        reverseList = new ArrayList<>();
    }

    @Override
    public void init(CustomerGroupFragmentView view) {
        this.view = view;
        initConnectors(rxBus, rxBusLocal);
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

                joinCustomerGroupWithCustomerOperations.addJoinCustomerGroupWithCustomer(joinCustomerGroupsWithCustomers).subscribe(aLong -> {
                    customerGroup.getCustomers().add(customers.get(position));
                });
            }
        } else {
            if (customerGroup.getName() == null) {
                reverseList.remove(customers.get(position));
            } else {
                String customerGroupId = customerGroup.getId();
                String customerId = customers.get(position).getId();

                joinCustomerGroupWithCustomerOperations.removeJoinCustomerGroupWithCustomer(customerGroupId, customerId).subscribe(aBoolean -> {
                    customerGroup.getCustomers().remove(customers.get(position));
                });
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

                joinCustomerGroupWithCustomerOperations.addJoinCustomerGroupWithCustomer(joinCustomerGroupsWithCustomers).subscribe(aLong -> {
                    customerGroup.getCustomers().add(c);

                    if (view != null) {
                        view.showCustomers(customerGroup, customers);
                    }
                });
            }

            reverseList = null;
        }
    }

    @Override
    public void showCustomers(CustomerGroup customerGroup) {
        customerOperations.getAllCustomers().subscribe(customers -> {
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
