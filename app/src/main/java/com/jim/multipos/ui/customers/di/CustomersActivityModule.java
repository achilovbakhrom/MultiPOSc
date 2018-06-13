package com.jim.multipos.ui.customers.di;

import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.core.BaseActivityModule;
import com.jim.multipos.ui.customers.CustomersActivity;
import com.jim.multipos.ui.customers.CustomersActivityPresenter;
import com.jim.multipos.ui.customers.CustomersActivityPresenterImpl;
import com.jim.multipos.ui.customers.CustomersActivityView;
import com.jim.multipos.ui.customers.customer.CustomerFragment;
import com.jim.multipos.ui.customers.customer.CustomerFragmentModule;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module(includes = BaseActivityModule.class)
public abstract class CustomersActivityModule {

    @Binds
    @PerActivity
    abstract AppCompatActivity provideCustomersActivity(CustomersActivity customersActivity);

    @Binds
    @PerActivity
    abstract CustomersActivityView provideCustomersActivityView(CustomersActivity customersActivity);

    @Binds
    @PerActivity
    abstract CustomersActivityPresenter provideCustomersActivityPresenter(CustomersActivityPresenterImpl presenter);

    @PerFragment
    @ContributesAndroidInjector(modules = CustomerFragmentModule.class)
    abstract CustomerFragment provideCustomerFragment();
}
