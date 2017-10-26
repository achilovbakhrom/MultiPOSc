package com.jim.multipos.ui.customer_group.di;

import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.core.BaseActivityModule;
import com.jim.multipos.ui.customer_group.CustomerGroupActivity;
import com.jim.multipos.ui.customer_group.fragments.AddCustomerGroupFragment;
import com.jim.multipos.ui.customer_group.fragments.AddCustomerGroupFragmentModule;
import com.jim.multipos.ui.customer_group.fragments.CustomerGroupFragment;
import com.jim.multipos.ui.customer_group.fragments.CustomerGroupFragmentModule;
import com.jim.multipos.ui.customer_group.fragments.CustomerGroupListFragment;
import com.jim.multipos.ui.customer_group.fragments.CustomerGroupListFragmentModule;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by user on 05.09.17.
 */

@Module(includes = BaseActivityModule.class)
public abstract class CustomerGroupActivityModule {
    @Binds
    @PerActivity
    abstract AppCompatActivity provideCustomerGroupActivity(CustomerGroupActivity activity);

    @PerFragment
    @ContributesAndroidInjector(modules = AddCustomerGroupFragmentModule.class)
    abstract AddCustomerGroupFragment provideAddCustomerGroupFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = CustomerGroupFragmentModule.class)
    abstract CustomerGroupFragment provideCustomerGroupFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = CustomerGroupListFragmentModule.class)
    abstract CustomerGroupListFragment provideCustomerGroupListFragment();
}
