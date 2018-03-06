package com.jim.multipos.ui.customers_edit_new;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.utils.rxevents.main_order_events.GlobalEventConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Portable-Acer on 04.11.2017.
 */

public class CustomersEditPresenterImpl extends BasePresenterImpl<CustomersEditView> implements CustomersEditPresenter {
    private DatabaseManager databaseManager;
    private List<Customer> customers;

    @Inject
    public CustomersEditPresenterImpl(CustomersEditView customersEditView, DatabaseManager databaseManager) {
        super(customersEditView);
        this.databaseManager = databaseManager;
    }

    @Override
    public Long getClientId() {
        if (customers.isEmpty()) {
            return 1L;
        }

        return customers.get(0).getClientId() + 1;
    }

    @Override
    public List<Customer> getCustomers() {
        this.customers = databaseManager.getAllCustomers().blockingSingle();

        return customers;
    }

    @Override
    public List<CustomerGroup> getCustomerGroups() {
        return databaseManager.getCustomerGroupOperations().getAllCustomerGroups().blockingSingle();
    }

    @Override
    public Long getQrCode() {
        return System.currentTimeMillis();
    }

    @Override
    public void addCustomer(String clientId, String fullName, String phone, String address, String qrCode, List<CustomerGroup> customerGroups) {
        Customer customer = new Customer();
        customer.setClientId(Long.parseLong(clientId));
        customer.setName(fullName);
        customer.setPhoneNumber(phone);
        customer.setAddress(address);
        customer.setQrCode(qrCode);
        customer.setCreatedDate(System.currentTimeMillis());
        customer.setCustomerGroups(customerGroups);

        databaseManager.getCustomerOperations().addCustomer(customer).subscribe(aLong -> {
            for (CustomerGroup cg : customerGroups) {
                databaseManager.getJoinCustomerGroupWithCustomerOperations().addCustomerToCustomerGroup(customer.getId(), cg.getId()).subscribe();
            }

            view.customerAdded(customer);
            view.sendEvent(GlobalEventConstants.ADD, customer);
        });
    }

    @Override
    public void updateCustomer(Customer customer) {
        databaseManager.getCustomerOperations().addCustomer(customer).subscribe(aLong -> {
            databaseManager.getJoinCustomerGroupWithCustomerOperations().removeJoinCustomerGroupWithCustomer(customer.getId()).subscribe(aBoolean -> {
                for (CustomerGroup cg : customer.getCustomerGroups()) {
                    databaseManager.getJoinCustomerGroupWithCustomerOperations().addCustomerToCustomerGroup(customer.getId(), cg.getId()).subscribe();
                }
            });
            view.sendEvent(GlobalEventConstants.UPDATE, customer);
        });
    }

    @Override
    public void removeCustomer(Customer customer) {
        customer.setActive(false);
        customer.setDeleted(true);
        databaseManager.getCustomerOperations().addCustomer(customer).subscribe(aBoolean -> {
            view.customerRemoved(customer);
            view.sendEvent(GlobalEventConstants.DELETE, customer);
        });
    }

    @Override
    public void sortByClientId() {
        Collections.sort(customers, (customer, t1) -> customer.getClientId().compareTo(t1.getClientId()));
        view.updateRecyclerView();
    }

    @Override
    public void sortByFullName() {
        Collections.sort(customers, (customer, t1) -> customer.getName().compareTo(t1.getName()));
        view.updateRecyclerView();
    }

    @Override
    public void sortByAddress() {
        Collections.sort(customers, (customer, t1) -> customer.getAddress().compareTo(t1.getAddress()));
        view.updateRecyclerView();
    }

    @Override
    public void sortByQrCode() {
        Collections.sort(customers, (customer, t1) -> customer.getQrCode().compareTo(t1.getQrCode()));
        view.updateRecyclerView();
    }

    @Override
    public void sortByDefault() {
        Collections.sort(customers, (customer, t1) -> t1.getClientId().compareTo(customer.getClientId()));
        view.updateRecyclerView();
    }

    @Override
    public boolean isCustomerExists(String name) {
        return databaseManager.getCustomerOperations().isCustomerExists(name).blockingSingle();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        databaseManager.getDaoSession().clear();
    }
}
