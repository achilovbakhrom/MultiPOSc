package com.jim.multipos.ui.customers.customer;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.customers.adapter.CustomersAdapter;
import com.jim.multipos.ui.start_configuration.payment_type.PaymentTypeFragment;
import com.jim.multipos.ui.start_configuration.payment_type.PaymentTypePresenterModule;
import com.jim.multipos.ui.start_configuration.payment_type.PaymentTypeView;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module(includes = CustomerFragmentPresenterModule.class)
public abstract class CustomerFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment provideFragment(CustomerFragment fragment);

    @Binds
    @PerFragment
    abstract CustomerFragmentView provideCustomerFragmentView(CustomerFragment fragment);

    @Provides
    @PerFragment
    static CustomersAdapter provideCustomersAdapter(AppCompatActivity context){
        return new CustomersAdapter(context);
    }
}
