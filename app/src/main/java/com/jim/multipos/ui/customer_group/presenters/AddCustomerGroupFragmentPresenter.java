package com.jim.multipos.ui.customer_group.presenters;

import com.jim.multipos.core.BaseFragmentPresenter;
import com.jim.multipos.ui.customer_group.fragments.AddCustomerGroupFragmentView;

/**
 * Created by user on 06.09.17.
 */

public interface AddCustomerGroupFragmentPresenter extends BaseFragmentPresenter<AddCustomerGroupFragmentView> {
    void addCustomerGroup(String groupName, int discountPosition, int serviceFeePosition, boolean isTaxFree, boolean isActive);
    void getServiceFees();
    void getCustomerGroup();
}
