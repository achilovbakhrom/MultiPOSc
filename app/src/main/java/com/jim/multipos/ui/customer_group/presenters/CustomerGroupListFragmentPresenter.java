package com.jim.multipos.ui.customer_group.presenters;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.customer.CustomerGroup;

/**
 * Created by user on 06.09.17.
 */

public interface CustomerGroupListFragmentPresenter extends Presenter {
    void getCustomerGroups();
    void itemClicked(int position);
    void addItemClicked();
    void customerGroupAdded();
    void deleteCustomerGroup(CustomerGroup customerGroup);
    void checkCustomerGroupName(String name);
    void customerGroupUpdate();
}
