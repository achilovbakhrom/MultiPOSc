package com.jim.multipos.ui.customers.customer;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerGroup;

import java.util.List;

public interface CustomerFragmentPresenter extends Presenter {
    void initData();
    void onCloseAction();
    void onAddPressed(String id, String name, String phone, String address, String qrCode, List<CustomerGroup> groups);
    void onSave(String name, String phone, String address, String qrCode, List<CustomerGroup> groups, Customer customer);
    void onDelete(Customer customer);
    void sortList(CustomerFragmentPresenterImpl.CustomerSortTypes customerSortTypes);
}
