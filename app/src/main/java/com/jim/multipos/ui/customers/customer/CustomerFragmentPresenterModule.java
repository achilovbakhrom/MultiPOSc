package com.jim.multipos.ui.customers.customer;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class CustomerFragmentPresenterModule {
    @Binds
    @PerFragment
    abstract CustomerFragmentPresenter provideCustomerFragmentPresenter(CustomerFragmentPresenterImpl presenter);

}
