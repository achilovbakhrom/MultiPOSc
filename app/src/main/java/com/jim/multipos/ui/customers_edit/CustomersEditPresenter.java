package com.jim.multipos.ui.customers_edit;

import com.jim.multipos.data.db.model.customer.CustomerGroup;

import java.util.List;

/**
 * Created by user on 02.09.17.
 */

public interface CustomersEditPresenter {
    void getQrCode();
    void getClientId();
    void getCustomers();
    void getCustomerGroups();
    void addCustomer(String clientId, String fullName, String phone, String address, String qrCode);
    void removeCustomer(int position);
    void refreshQrCode(int position);
    void saveCustomer(int position, String fullName, String phone, String address, String qrCode);
    void save();
    void cancel();
    void getDialogData();
    void customerGroupsSelected(List<CustomerGroup> selectedItems);
    //void getCustomerGroupJoinToCustomer();
    void getCustomerCustomerGroups(int position);
    void updateCustomerCustomerGroup(int position, List<CustomerGroup> selectedItems);
    //void sortCustomerGroups(int position);
    void filterCustomerGroups(int position);
}
