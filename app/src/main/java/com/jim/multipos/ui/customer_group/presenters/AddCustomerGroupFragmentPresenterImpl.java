package com.jim.multipos.ui.customer_group.presenters;

import android.content.Context;
import android.util.Log;

import com.jim.multipos.R;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.data.operations.CustomerGroupOperations;
import com.jim.multipos.data.operations.ServiceFeeOperations;
import com.jim.multipos.ui.customer_group.connector.AddCustomerGroupConnector;
import com.jim.multipos.ui.customer_group.connector.CustomerGroupConnector;
import com.jim.multipos.ui.customer_group.connector.CustomerGroupListConnector;
import com.jim.multipos.ui.customer_group.fragments.AddCustomerGroupFragmentView;
import com.jim.multipos.ui.customers_edit.connector.CustomersEditConnector;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.rxevents.CustomerGroupEvent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

/**
 * Created by user on 06.09.17.
 */

public class AddCustomerGroupFragmentPresenterImpl extends AddCustomerGroupConnector implements AddCustomerGroupFragmentPresenter {
    private AddCustomerGroupFragmentView view;
    private Context context;
    private CustomerGroupOperations customerGroupOperations;
    private ServiceFeeOperations serviceFeeOperations;
    private List<ServiceFee> serviceFees;
    private CustomerGroup currentCustomerGroup;
    private RxBusLocal rxBusLocal;
    private RxBus rxBus;

    public AddCustomerGroupFragmentPresenterImpl(Context context, CustomerGroupOperations customerGroupOperations, ServiceFeeOperations serviceFeeOperations, RxBus rxBus, RxBusLocal rxBusLocal) {
        this.context = context;
        this.customerGroupOperations = customerGroupOperations;
        this.serviceFeeOperations = serviceFeeOperations;
        this.rxBusLocal = rxBusLocal;
        this.rxBus = rxBus;
    }

    @Override
    public void init(AddCustomerGroupFragmentView view) {
        this.view = view;
        initConnectors(rxBus, rxBusLocal);
    }

    @Override
    public void showCustomerGroup(CustomerGroup customerGroup) {
        currentCustomerGroup = customerGroup;
        view.showCustomerGroup(currentCustomerGroup);
    }

    @Override
    public void showCustomerGroup() {
        rxBusLocal.send(new CustomerGroupEvent(currentCustomerGroup, CustomerGroupConnector.SHOW_CUSTOMERS));
    }

    @Override
    public void addCustomerGroup(String groupName, int discountPosition, int serviceFeePosition, boolean isTaxFree, boolean isActive) {
        boolean hasError = checkData(groupName);
        CustomerGroup customerGroup;

        if (!hasError) {
            if (currentCustomerGroup != null) {
                customerGroup = currentCustomerGroup;
            } else {
                customerGroup = new CustomerGroup();
            }

            customerGroup.setName(groupName);
            customerGroup.setServiceFee(serviceFees.get(serviceFeePosition));
            customerGroup.setIsTaxFree(isTaxFree);
            customerGroup.setIsActive(isActive);

            customerGroupOperations.addCustomerGroup(customerGroup).subscribe(aLong -> {
                view.clearViews();
                rxBusLocal.send(new CustomerGroupEvent(customerGroup, CustomerGroupListConnector.CUSTOMER_GROUP_ADDED));
                rxBus.send(new CustomerGroupEvent(customerGroup, CustomersEditConnector.CUSTOMER_GROUP_ADDED));
                currentCustomerGroup = null;
            });
        }
    }

    @Override
    public void getCustomerGroup() {
        view.showMembers();
    }

    @Override
    public void addCustomerGroupClicked() {
        currentCustomerGroup = null;
        view.clearViews();
        view.requestFocus();
    }

    @Override
    public void getServiceFees() {
        serviceFeeOperations.getAllServiceFees().subscribe(serviceFees -> {
            this.serviceFees = serviceFees;
        });

        view.showServiceFees(this.serviceFees);
    }

    private boolean checkData(String groupName) {
        boolean hasError = false;

        if (groupName.isEmpty()) {
            hasError = true;
            view.showGroupNameError(getString(R.string.enter_group_name));
        }

        return hasError;
    }

    private String getString(int resId) {
        return context.getString(resId);
    }
}
