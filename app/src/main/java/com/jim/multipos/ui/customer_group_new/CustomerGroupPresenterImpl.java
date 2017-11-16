package com.jim.multipos.ui.customer_group_new;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

/**
 * Created by Portable-Acer on 03.11.2017.
 */

public class CustomerGroupPresenterImpl extends BasePresenterImpl<CustomerGroupView> implements CustomerGroupPresenter {
    private DatabaseManager databaseManager;
    private List<Customer> addCustomers;

    @Inject
    public CustomerGroupPresenterImpl(CustomerGroupView customerGroupView, DatabaseManager databaseManager) {
        super(customerGroupView);
        this.databaseManager = databaseManager;
        addCustomers = new ArrayList<>();
    }

    @Override
    public List<CustomerGroup> getCustomerGroups() {
        List<CustomerGroup> customerGroups = databaseManager.getCustomerGroupOperations().getAllCustomerGroups().blockingSingle();

        return customerGroups;
    }

    @Override
    public CustomerGroup getCustomerGroupById(long id) {
        return databaseManager.getCustomerGroupOperations().getCustomerGroupById(id).blockingSingle();
    }

    @Override
    public List<Customer> getCustomers() {
        return databaseManager.getCustomerOperations().getAllCustomers().blockingSingle();
    }

    @Override
    public void clearAddFragment() {
        view.clearAddFragment();
    }

    @Override
    public void showSelectedCustomerGroup(int position) {
        databaseManager.getCustomerGroupOperations().getAllCustomerGroups().subscribe(customerGroups -> view.showSelectedCustomerGroup(customerGroups.get(position)));
    }

    @Override
    public void updateCustomerGroup(CustomerGroup customerGroup) {
        databaseManager.getCustomerGroupOperations().addCustomerGroup(customerGroup).subscribe(aLong -> {
            view.updateItem(customerGroup);
        });
    }

    @Override
    public void addCustomerGroup(String name, boolean isActive) {
        CustomerGroup customerGroup = new CustomerGroup();
        customerGroup.setName(name);
        customerGroup.setIsActive(isActive);
        customerGroup.setCreatedDate(System.currentTimeMillis());

        databaseManager.getCustomerGroupOperations().addCustomerGroup(customerGroup).subscribe(aLong -> {
            for (Customer c : addCustomers) {
                databaseManager.getJoinCustomerGroupWithCustomerOperations().addCustomerToCustomerGroup(customerGroup.getId(), c.getId()).blockingSingle();
            }

            addCustomers.clear();
            view.addItem(customerGroup);
        });
    }

    @Override
    public void removeCustomerGroup(CustomerGroup customerGroup) {
        if (customerGroup.getIsActive()) {
            view.showActiveCustomerGroupWarningDialog();
        } else {
            databaseManager.getCustomerGroupOperations().removeCustomerGroup(customerGroup).subscribe(aBoolean -> {
                view.removeItem(customerGroup);
            });
        }
    }

    @Override
    public boolean isCustomerGroupExists(String name) {
        return databaseManager.getCustomerGroupOperations().isCustomerGroupExists(name).blockingSingle();
    }

    @Override
    public void addCustomerToCustomerGroup(CustomerGroup customerGroup, Customer customer) {
        if (customerGroup.getId() == -1) {
            addCustomers.add(customer);
            view.addedCustomerToCustomerGroup(customer);
        } else {
            databaseManager.getJoinCustomerGroupWithCustomerOperations().addCustomerToCustomerGroup(customerGroup.getId(), customer.getId()).subscribe(aLong -> {
                view.addedCustomerToCustomerGroup(customer);
            });
        }
    }

    @Override
    public void removeCustomerFromCustomerGroup(CustomerGroup customerGroup, Customer customer) {
        if (customerGroup.getId() == -1) {
            addCustomers.remove(customer);
            view.removedCustomerFromCustomerGroup(customer);
        } else {
            databaseManager.getJoinCustomerGroupWithCustomerOperations().removeCustomerFromCustomerGroup(customerGroup.getId(), customer.getId()).subscribe(aBoolean -> {
                view.removedCustomerFromCustomerGroup(customer);
            });
        }
    }
}
