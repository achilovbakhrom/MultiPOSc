package com.jim.multipos.ui.customers_edit;

import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.ui.customers_edit.entity.CustomersWithCustomerGroups;

import java.util.List;

/**
 * Created by user on 02.09.17.
 */

public interface CustomersEditView {
    void clearViews();
    void showQrCode(String qrCode);
    void showClientId(String clientId);
    void showCustomersWithCustomerGroups(List<CustomersWithCustomerGroups> customersWithCustomerGroups);
    void showCustomerGroups(List<CustomerGroup> customerGroups);
    void showFullNameError(String error);
    void showPhoneError(String error);
    void showAddressError(String error);
    void updateRecyclerView();
    void requestFocus();
    void save();
    void cancel();
    void showCustomerGroupDialog(List<CustomerGroup> customerGroups, List<CustomerGroup> selectedCustomerGroups);
    void showSelectedCustomerGroups(String selectedCustomerGroups);
    void showRVItemCustomerGroupDialog(int position, List<CustomerGroup> customerGroups, List<CustomerGroup> selectedCustomerGroups);
    void updateCustomerGroupDialog();
}
