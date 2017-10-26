package com.jim.multipos.ui.customers_edit;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.customer.CustomerGroup;

import java.util.List;

/**
 * Created by user on 02.09.17.
 */

public interface CustomersEditPresenter extends Presenter {
    /*void getQrCode();
    void getClientId();*/
    void showClientId();
    void getCustomers();
    void getCustomerGroups();
    void removeCustomer(int position);
    void refreshQrCode(int position);
    void refreshQrCode();
    void saveCustomer(int position, String fullName, String phone, String address, String qrCode);
    void save();
    void back();
    void showCustomerGroupDialog();
    void customerGroupsSelected(List<CustomerGroup> selectedItems);
    void getCustomerCustomerGroups(int position);
    void getCustomerCustomerGroups();
    void updateCustomerCustomerGroup(int position, List<CustomerGroup> selectedItems);
    void updateCustomerCustomerGroup(List<CustomerGroup> selectedItems);
    void filterCustomerGroups(int position);
    void showCustomerGroups();
    void addCustomer(String clientId, String fullName, String phone, String address, String qrCode, List<CustomerGroup> selectedGroups);
    void sortByName();
    void sortByPhone();
    void sortByAddress();
    void sortByClientId();
    void sortByQrCode();
    void sortByIdDesc();
    boolean isCustomerExists(String name);
}
