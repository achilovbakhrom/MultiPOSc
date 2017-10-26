package com.jim.multipos.ui.customer_group.fragments;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.customer_group.presenters.AddCustomerGroupFragmentPresenterModule;

import dagger.Binds;
import dagger.Module;

/**
 * Created by user on 18.10.17.
 */
@Module(includes = {AddCustomerGroupFragmentPresenterModule.class})
public abstract class AddCustomerGroupFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment provideFragment(AddCustomerGroupFragment fragment);

    @Binds
    @PerFragment
    abstract AddCustomerGroupFragmentView provideAddCustomerGroupFragmentView(AddCustomerGroupFragment fragment);
}
