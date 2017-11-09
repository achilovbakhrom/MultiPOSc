package com.jim.multipos.ui.customer_group.presenters;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.ui.customer_group.connector.CustomerGroupConnector;
import com.jim.multipos.ui.customer_group.connector.CustomerGroupListConnector;
import com.jim.multipos.ui.customer_group.fragments.AddCustomerGroupFragmentView;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.rxevents.CustomerGroupEvent;

import javax.inject.Inject;
import javax.inject.Named;

import static com.jim.multipos.ui.customer_group.fragments.CustomerGroupListFragment.CUSTOMER_GROUP_DELETE;
import static com.jim.multipos.ui.customer_group.fragments.CustomerGroupListFragment.CUSTOMER_GROUP_NAME;

/**
 * Created by user on 06.09.17.
 */

public class AddCustomerGroupFragmentPresenterImpl extends BasePresenterImpl<AddCustomerGroupFragmentView> implements AddCustomerGroupFragmentPresenter {
    @Inject
    RxBusLocal rxBusLocal;
    @Inject
    RxBus rxBus;
    private AddCustomerGroupFragmentView view;
    private CustomerGroup currentCustomerGroup;
    private DatabaseManager databaseManager;
    private String enterGroupName;

    @Inject
    public AddCustomerGroupFragmentPresenterImpl(AddCustomerGroupFragmentView view, DatabaseManager databaseManager,
                                                 @Named(value = "enter_group_name") String enterGroupName) {
        super(view);
        this.view = view;
        this.databaseManager = databaseManager;
        this.enterGroupName = enterGroupName;
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
    public void addCustomerGroup(String groupName, boolean isActive) {
        databaseManager.getCustomerGroupOperations().isCustomerGroupExists(groupName).subscribe(isCustomerGroupExists -> {

            if (!isCustomerGroupExists || (currentCustomerGroup != null && currentCustomerGroup.getName().equals(groupName))) {
                CustomerGroup customerGroup;

                if (currentCustomerGroup != null) {
                    customerGroup = currentCustomerGroup;
                } else {
                    customerGroup = new CustomerGroup();
                }

                customerGroup.setName(groupName);
                customerGroup.setIsActive(isActive);

                databaseManager.getCustomerGroupOperations().addCustomerGroup(customerGroup).subscribe(aLong -> {
                    view.clearViews();
                    view.changeButtonNameAndVisibility();
                    rxBusLocal.send(new CustomerGroupEvent(customerGroup, CustomerGroupListConnector.CUSTOMER_GROUP_ADDED));
                    currentCustomerGroup = null;
                });
            } else {
                view.showGroupNameExistError();
            }
        });
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
    public void deleteCustomerGroup() {
        if (currentCustomerGroup != null && !currentCustomerGroup.getIsActive()) {
            rxBusLocal.send(new CustomerGroupEvent(currentCustomerGroup, CUSTOMER_GROUP_DELETE));
        } else {
            view.showItemActiveCustomerGroupWarningDialog();
        }
    }

    @Override
    public void getCustomerGroupName(String name) {
        CustomerGroup customerGroup = new CustomerGroup();
        customerGroup.setName(name);
        rxBusLocal.send(new CustomerGroupEvent(customerGroup, CUSTOMER_GROUP_NAME));
    }

    @Override
    public void customerGroupDeleted() {
        currentCustomerGroup = null;
        view.clearViews();
    }
/*private boolean checkData(String groupName) {
        boolean hasError = false;

        if (groupName.isEmpty()) {
            hasError = true;
            view.showGroupNameError(enterGroupName);
        }

        return hasError;
    }*/
}
