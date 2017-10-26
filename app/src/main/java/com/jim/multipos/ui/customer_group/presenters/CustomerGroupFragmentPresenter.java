package com.jim.multipos.ui.customer_group.presenters;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.customer.CustomerGroup;

/**
 * Created by user on 07.09.17.
 */

public interface CustomerGroupFragmentPresenter extends Presenter {
    void itemChecked(int position, boolean checked);
    void showCustomers(CustomerGroup customerGroup);
    void customerGroupAdded(CustomerGroup customerGroup);
}