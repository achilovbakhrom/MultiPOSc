package com.jim.multipos.ui.customer_group.di;

import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.ui.ActivityScope;
import com.jim.multipos.ui.customer_group.CustomerGroupActivity;
import com.jim.multipos.ui.customer_group.CustomerGroupActivityPresenter;
import com.jim.multipos.ui.customer_group.CustomerGroupActivityPresenterImpl;
import com.jim.multipos.ui.customer_group.presenters.AddCustomerGroupFragmentPresenter;
import com.jim.multipos.ui.customer_group.presenters.AddCustomerGroupFragmentPresenterImpl;
import com.jim.multipos.ui.customer_group.presenters.CustomerGroupFragmentPresenter;
import com.jim.multipos.ui.customer_group.presenters.CustomerGroupFragmentPresenterImpl;
import com.jim.multipos.ui.customer_group.presenters.CustomerGroupListFragmentPresenter;
import com.jim.multipos.ui.customer_group.presenters.CustomerGroupListFragmentPresenterImpl;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.managers.PosFragmentManager;

import dagger.Module;
import dagger.Provides;

/**
 * Created by user on 05.09.17.
 */

@Module
public class CustomerGroupActivityModule {
    private CustomerGroupActivity activity;

    public CustomerGroupActivityModule(CustomerGroupActivity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    public CustomerGroupActivity provideActivity() {
        return activity;
    }

    @Provides
    @ActivityScope
    public CustomerGroupActivityPresenter getCustomerGroupPresenter(RxBus rxBus, CustomerGroupActivity activity, DatabaseManager databaseManager) {
        return new CustomerGroupActivityPresenterImpl(rxBus, activity, activity, databaseManager.getCustomerGroupOperations());
    }

    @Provides
    @ActivityScope
    public AddCustomerGroupFragmentPresenter getAddCustomerGroupFragmentPresenter(CustomerGroupActivity activity, DatabaseManager databaseManager, RxBus rxBus, RxBusLocal rxBusLocal) {
        return new AddCustomerGroupFragmentPresenterImpl(activity, databaseManager.getCustomerGroupOperations(), databaseManager.getServiceFeeOperations(), rxBus, rxBusLocal);
    }

    @Provides
    @ActivityScope
    public CustomerGroupListFragmentPresenter getCustomerGroupListFragmentPresenter(DatabaseManager databaseManager, RxBus rxBus, RxBusLocal rxBusLocal) {
        return new CustomerGroupListFragmentPresenterImpl(databaseManager.getCustomerGroupOperations(), rxBus, rxBusLocal);
    }

    @Provides
    @ActivityScope
    public CustomerGroupFragmentPresenter getCusomterGroupFragmentPresenter(DatabaseManager databaseManager, RxBus rxBus, RxBusLocal rxBusLocal) {
        return new CustomerGroupFragmentPresenterImpl(databaseManager.getCustomerOperations(), databaseManager.getJoinCustomerGroupWithCustomerOperations(), rxBus, rxBusLocal);
    }

    @Provides
    @ActivityScope
    public PosFragmentManager getFragmentManager(CustomerGroupActivity activity) {
        return new PosFragmentManager(activity);
    }

    @Provides
    @ActivityScope
    public RxBusLocal provideRxBusLocal() {
        return new RxBusLocal();
    }
}
