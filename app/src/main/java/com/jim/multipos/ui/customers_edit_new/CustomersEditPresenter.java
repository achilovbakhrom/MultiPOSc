package com.jim.multipos.ui.customers_edit_new;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerGroup;

import java.util.List;

/**
 * Created by Portable-Acer on 04.11.2017.
 */

public interface CustomersEditPresenter extends Presenter {
    List<Customer> getCustomers();
    List<CustomerGroup> getCustomerGroups();
    Long getClientId();
    Long getQrCode();
    void addCustomer(String clientId, String fullName, String phone, String address, String qrCode, List<CustomerGroup> customerGroups);
    void updateCustomer(Customer customer);
    void removeCustomer(Customer customer);
    void sortByClientId();
    void sortByFullName();
    void sortByAddress();
    void sortByQrCode();
    void sortByDefault();
    boolean isCustomerExists(String name);
}
