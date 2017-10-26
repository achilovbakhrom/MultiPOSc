package com.jim.multipos.ui.customer_group.presenters;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

/**
 * Created by user on 18.10.17.
 */

@Module
public abstract class CustomerGroupListFragmentPresenterModule {
    @Binds
    @PerFragment
    abstract CustomerGroupListFragmentPresenter provideCustomerGroupListFragmentPresenter(CustomerGroupListFragmentPresenterImpl presenter);
}