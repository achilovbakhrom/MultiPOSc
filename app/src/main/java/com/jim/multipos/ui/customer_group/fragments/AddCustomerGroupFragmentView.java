package com.jim.multipos.ui.customer_group.fragments;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerGroup;

import java.util.List;

/**
 * Created by user on 06.09.17.
 */

public interface AddCustomerGroupFragmentView extends BaseView {
    void showGroupNameExistError();
    void showCustomerGroup(CustomerGroup customerGroup);
    void showMembers();
    void clearViews();
    void requestFocus();
    void showItemActiveCustomerGroupWarningDialog();
    void changeButtonNameAndVisibility();
}
