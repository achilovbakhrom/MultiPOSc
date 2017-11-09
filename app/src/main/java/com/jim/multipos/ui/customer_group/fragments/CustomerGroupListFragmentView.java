package com.jim.multipos.ui.customer_group.fragments;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.customer.CustomerGroup;

import java.util.List;

/**
 * Created by user on 06.09.17.
 */

public interface CustomerGroupListFragmentView extends BaseView {
    void showCustomerGroups(List<CustomerGroup> customerGroups, int selectedPosition);
    void showCustomerGroupRemoveWarningDialog();
    void showCustomerGroupSaveDialog();
    void updateRV(int position);
}
