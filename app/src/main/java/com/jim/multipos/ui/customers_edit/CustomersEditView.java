package com.jim.multipos.ui.customers_edit;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.ui.customers_edit.entity.CustomersWithCustomerGroups;

import java.util.List;

/**
 * Created by user on 02.09.17.
 */

public interface CustomersEditView extends BaseView {
    /*void clearViews();
    void showQrCode(String qrCode);
    void showClientId(String clientId);*/
    void showCustomersWithCustomerGroups(List<CustomerGroup> customerTypeSelectedCustomerGroups, List<CustomersWithCustomerGroups> customersWithCustomerGroups, String clientId, String qrCode);
    void showCustomerGroups(List<CustomerGroup> customerGroups);
    /*void showFullNameError(String error);
    void showPhoneError(String error);
    void showAddressError(String error);*/
    void updateRecyclerView();
    /*void requestFocus();*/
    void save();
    void cancel();
    void showCustomerGroupDialog(List<CustomerGroup> customerGroups, List<CustomerGroup> selectedCustomerGroups);
    /*void showSelectedCustomerGroups(String selectedCustomerGroups);*/
    void showRVItemCustomerGroupDialog(int position, List<CustomerGroup> customerGroups, List<CustomerGroup> selectedCustomerGroups);
    void showRVItemCustomerGroupDialog(List<CustomerGroup> customerGroups, List<CustomerGroup> selectedCustomerGroups);
    void updateCustomerGroupDialog();
    void updateCustomerTypeQrCode(String qrCode);
    void updateRecyclerView(List<CustomersWithCustomerGroups> customersWithCustomerGroups);
    void showClientId(String clientId);
}
