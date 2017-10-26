package com.jim.multipos.ui.customer_group.fragments;


import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.customer_group.presenters.CustomerGroupFragmentPresenterModule;

import dagger.Binds;
import dagger.Module;

/**
 * Created by user on 18.10.17.
 */
@Module(includes = {CustomerGroupFragmentPresenterModule.class})
public abstract class CustomerGroupFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment provideFragment(CustomerGroupFragment fragment);

    @Binds
    @PerFragment
    abstract CustomerGroupFragmentView provideCustomerGroupFragmentView(CustomerGroupFragment fragment);
}
