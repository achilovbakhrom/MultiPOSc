package com.jim.multipos.ui.customer_debt.presenter;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Sirojiddin on 29.12.2017.
 */
@Module
public abstract class CustomerListPresenterModule {
    @Binds
    @PerFragment
    abstract CustomerListPresenter provideCustomerListPresenter(CustomerListPresenterImpl presenter);
}
