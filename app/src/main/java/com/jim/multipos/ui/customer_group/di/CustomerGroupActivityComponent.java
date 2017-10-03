package com.jim.multipos.ui.customer_group.di;

import com.jim.multipos.ui.ActivityScope;
import com.jim.multipos.ui.customer_group.CustomerGroupActivity;
import com.jim.multipos.ui.customer_group.fragments.AddCustomerGroupFragment;
import com.jim.multipos.ui.customer_group.fragments.CustomerGroupFragment;
import com.jim.multipos.ui.customer_group.fragments.CustomerGroupListFragment;

import dagger.Subcomponent;

/**
 * Created by user on 05.09.17.
 */
@ActivityScope
@Subcomponent(modules = {CustomerGroupActivityModule.class})
public interface CustomerGroupActivityComponent {
    void inject(CustomerGroupActivity activity);
    void inject(AddCustomerGroupFragment fragment);
    void inject(CustomerGroupListFragment fragment);
    void inject(CustomerGroupFragment fragment);
}
