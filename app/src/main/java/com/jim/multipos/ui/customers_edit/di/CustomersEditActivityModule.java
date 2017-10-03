package com.jim.multipos.ui.customers_edit.di;

import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.ui.ActivityScope;
import com.jim.multipos.ui.customers_edit.CustomersEditActivity;
import com.jim.multipos.ui.customers_edit.CustomersEditPresenter;
import com.jim.multipos.ui.customers_edit.CustomersEditPresenterImpl;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;

import dagger.Module;
import dagger.Provides;

/**
 * Created by user on 02.09.17.
 */

@Module
public class CustomersEditActivityModule {
    private CustomersEditActivity activity;

    public CustomersEditActivityModule(CustomersEditActivity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    public  CustomersEditActivity provideActivity() {
        return activity;
    }

    @Provides
    @ActivityScope
    public CustomersEditPresenter getCustomersEditPresenter(RxBus rxBus, RxBusLocal rxBusLocal, CustomersEditActivity activity, DatabaseManager manager) {
        return new CustomersEditPresenterImpl(rxBus, rxBusLocal, activity, activity, manager.getCustomerOperations(), manager.getCustomerGroupOperations(), manager.getJoinCustomerGroupWithCustomerOperations());
    }

    @Provides
    @ActivityScope
    public RxBusLocal provideRxBusLocal() {
        return new RxBusLocal();
    }
}
