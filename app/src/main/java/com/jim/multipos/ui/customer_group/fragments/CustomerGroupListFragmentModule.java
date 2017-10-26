package com.jim.multipos.ui.customer_group.fragments;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.customer_group.presenters.CustomerGroupListFragmentPresenterModule;

import dagger.Binds;
import dagger.Module;

/**
 * Created by user on 18.10.17.
 */

@Module(includes = {CustomerGroupListFragmentPresenterModule.class})
public abstract class CustomerGroupListFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment provideFragment(CustomerGroupListFragment fragment);

    @Binds
    @PerFragment
    abstract CustomerGroupListFragmentView provideCustomerGroupListFragmentView(CustomerGroupListFragment fragment);
}