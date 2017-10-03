package com.jim.multipos.ui.main_menu.customers_menu.di;

import com.jim.multipos.ui.ActivityScope;
import com.jim.multipos.ui.main_menu.customers_menu.CustomersMenuActivity;
import com.jim.multipos.ui.main_menu.customers_menu.presenters.CustomerMenuPresenterImpl;
import com.jim.multipos.ui.main_menu.customers_menu.presenters.CustomersMenuPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by DEV on 08.08.2017.
 */
@Module
public class CustomersMenuModule {
    private CustomersMenuActivity activity;

    public CustomersMenuModule(CustomersMenuActivity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    public CustomersMenuActivity getActivity() {
        return activity;
    }


    @Provides
    @ActivityScope
    public CustomersMenuPresenter getMenuProductPresenter() {
        return new CustomerMenuPresenterImpl();
    }
}
