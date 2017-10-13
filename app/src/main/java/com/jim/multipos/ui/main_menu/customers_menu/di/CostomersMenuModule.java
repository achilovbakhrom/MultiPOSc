package com.jim.multipos.ui.main_menu.customers_menu.di;

import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.core.BaseActivityModule;
import com.jim.multipos.ui.main_menu.customers_menu.CustomersMenuActivity;
import com.jim.multipos.ui.main_menu.customers_menu.CustomersMenuView;
import com.jim.multipos.ui.main_menu.customers_menu.presenters.CustomerMenuPresenterImpl;
import com.jim.multipos.ui.main_menu.customers_menu.presenters.CustomersMenuPresenter;

import dagger.Binds;
import dagger.Module;

/**
 * Created by bakhrom on 10/6/17.
 */

@Module(includes = BaseActivityModule.class)
public abstract class CostomersMenuModule {

    @Binds
    @PerActivity
    abstract AppCompatActivity provideCustomersMenuActivity(CustomersMenuActivity customersMenuActivity);


    @Binds
    @PerActivity
    abstract CustomersMenuPresenter provideCustomersMenuPresenter(CustomerMenuPresenterImpl customerMenuPresenter);

    @Binds
    @PerActivity
    abstract CustomersMenuView provideCustomersMenuView(CustomersMenuActivity customersMenuActivity);

}
