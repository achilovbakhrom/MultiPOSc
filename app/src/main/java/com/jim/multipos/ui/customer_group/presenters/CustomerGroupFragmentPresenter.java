package com.jim.multipos.ui.customer_group.presenters;

import com.jim.multipos.core.BaseFragmentPresenter;
import com.jim.multipos.ui.customer_group.fragments.CustomerGroupFragmentView;

/**
 * Created by user on 07.09.17.
 */

public interface CustomerGroupFragmentPresenter extends BaseFragmentPresenter<CustomerGroupFragmentView> {
    void itemChecked(int position, boolean checked);
}