package com.jim.multipos.ui.customers.customer;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.ui.customers.model.CustomerAdapterDetails;
import com.jim.multipos.utils.rxevents.main_order_events.GlobalEventConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class CustomerFragmentPresenterImpl extends BasePresenterImpl<CustomerFragmentView> implements CustomerFragmentPresenter {

    private DatabaseManager databaseManager;
    private List<CustomerAdapterDetails> items;

    public enum CustomerSortTypes {Id, Name, Address, Phone, Default, QrCode}

    @Inject
    protected CustomerFragmentPresenterImpl(CustomerFragmentView customerFragmentView, DatabaseManager databaseManager) {
        super(customerFragmentView);
        this.databaseManager = databaseManager;
        items = new ArrayList<>();
    }

    @Override
    public void initData() {
        items.clear();
        databaseManager.getCustomers().subscribe(customers -> {
            items.add(null);
            for (int i = 0; i < customers.size(); i++) {
                customers.get(i).resetCustomerGroups();
                CustomerAdapterDetails customerAdapterDetails = new CustomerAdapterDetails();
                customerAdapterDetails.setObject(customers.get(i));
                items.add(customerAdapterDetails);
            }
            List<Customer> customers1 = databaseManager.getAllCustomers().blockingGet();
            long id = 1L;
            if (!customers1.isEmpty()) {
                id = customers1.get(customers1.size() - 1).getClientId() + 1;
            }
            view.refreshList(items, databaseManager.getAllCustomerGroups().blockingSingle(), id);
        });
    }

    @Override
    public void onAddPressed(String id, String name, String phone, String address, String qrCode, List<CustomerGroup> groups) {
        Customer customer = new Customer();
        customer.setClientId(Long.valueOf(id));
        customer.setName(name);
        customer.setPhoneNumber(phone);
        customer.setAddress(address);
        customer.setQrCode(qrCode);
        databaseManager.addCustomer(customer).subscribe(aLong -> {
            CustomerAdapterDetails customerAdapterDetails = new CustomerAdapterDetails();
            customerAdapterDetails.setObject(customer);
            items.add(1, customerAdapterDetails);
            view.notifyItemAdd(1);
            for (CustomerGroup cg : groups) {
                databaseManager.getJoinCustomerGroupWithCustomerOperations().addCustomerToCustomerGroup(customer.getId(), cg.getId()).subscribe();
            }
            view.sendEvent(GlobalEventConstants.ADD, customer);
        });
    }

    @Override
    public void onSave(String name, String phone, String address, String qrCode, List<CustomerGroup> groups, Customer customer) {
        customer.setName(name);
        customer.setPhoneNumber(phone);
        customer.setAddress(address);
        customer.setQrCode(qrCode);
        databaseManager.getJoinCustomerGroupWithCustomerOperations().removeJoinCustomerGroupWithCustomer(customer.getId()).subscribe();
        for (CustomerGroup cg : groups) {
            databaseManager.getJoinCustomerGroupWithCustomerOperations().addCustomerToCustomerGroup(customer.getId(), cg.getId()).subscribe();
        }
        databaseManager.addCustomer(customer).subscribe(aLong -> {
            for (int i = 1; i < items.size(); i++) {
                if (items.get(i).getObject().getId().equals(customer.getId())) {
                    CustomerAdapterDetails customerAdapterDetails = new CustomerAdapterDetails();
                    customerAdapterDetails.setObject(customer);
                    items.set(i, customerAdapterDetails);
                    view.notifyItemChanged(i);
                    return;
                }
            }
            view.sendEvent(GlobalEventConstants.UPDATE, customer);
        });
    }

    @Override
    public void onDelete(Customer customer) {
        customer.setActive(false);
        customer.setDeleted(true);
        databaseManager.getCustomerOperations().addCustomer(customer).subscribe(aBoolean -> {
            for (int i = 1; i < items.size(); i++) {
                if (items.get(i).getObject().getId().equals(customer.getId())) {
                    items.remove(i);
                    view.notifyItemRemove(i);
                    break;
                }
            }
            view.sendEvent(GlobalEventConstants.DELETE, customer);
        });
    }

    @Override
    public void sortList(CustomerSortTypes customerSortTypes) {
        items.remove(0);
        switch (customerSortTypes) {
            case Address:
                Collections.sort(items, (discounts, t1) -> t1.getObject().getAddress().compareTo(discounts.getObject().getAddress()));
                break;
            case Id:
                Collections.sort(items, (discounts, t1) -> t1.getObject().getClientId().compareTo(discounts.getObject().getClientId()));
                break;
            case Name:
                Collections.sort(items, (discounts, t1) -> t1.getObject().getName().compareTo(discounts.getObject().getName()));
                break;
            case QrCode:
                Collections.sort(items, (discounts, t1) -> t1.getObject().getQrCode().compareTo(discounts.getObject().getQrCode()));
                break;
            case Phone:
                Collections.sort(items, (discounts, t1) -> t1.getObject().getPhoneNumber().compareTo(discounts.getObject().getPhoneNumber()));
                break;
            case Default:
                Collections.sort(items, (discounts, t1) -> t1.getObject().getCreatedDate().compareTo(discounts.getObject().getCreatedDate()));
                break;
        }
        items.add(0, null);
        view.refreshList();
    }

    @Override
    public void onCloseAction() {
        boolean canBeClosed = true;
        for (int i = 1; i < items.size(); i++) {
            if (items.get(i).isChanged())
                canBeClosed = false;
        }
        if (canBeClosed) {
            view.closeActivity();
        } else {
            view.openWarning();
        }
    }

}
