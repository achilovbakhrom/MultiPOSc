package com.jim.multipos.ui.customer_group.presenters;

import com.jim.multipos.core.BaseFragmentPresenter;
import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.ui.customer_group.fragments.AddCustomerGroupFragmentView;

/**
 * Created by user on 06.09.17.
 */

public interface AddCustomerGroupFragmentPresenter extends Presenter {
    void addCustomerGroup(String groupName, boolean isActive);
    void getCustomerGroup();
    void showCustomerGroup(CustomerGroup customerGroup);
    void showCustomerGroup();
    void addCustomerGroupClicked();
    void deleteCustomerGroup();
    void getCustomerGroupName(String name);
    void customerGroupDeleted();
}
