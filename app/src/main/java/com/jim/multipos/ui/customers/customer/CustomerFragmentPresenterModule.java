package com.jim.multipos.ui.customers.customer;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.start_configuration.payment_type.PaymentTypePresenter;
import com.jim.multipos.ui.start_configuration.payment_type.PaymentTypePresenterImpl;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class CustomerFragmentPresenterModule {
    @Binds
    @PerFragment
    abstract CustomerFragmentPresenter provideCustomerFragmentPresenter(CustomerFragmentPresenterImpl presenter);

}
