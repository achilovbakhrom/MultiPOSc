package com.jim.multipos.ui.customer_group.presenters;

import com.jim.multipos.core.BaseFragmentPresenter;
import com.jim.multipos.ui.customer_group.fragments.CustomerGroupListFragmentView;

/**
 * Created by user on 06.09.17.
 */

public interface CustomerGroupListFragmentPresenter extends BaseFragmentPresenter<CustomerGroupListFragmentView> {
    void getCustomerGroups();
    void itemClicked(int position);
    void addItemClicked();
}
