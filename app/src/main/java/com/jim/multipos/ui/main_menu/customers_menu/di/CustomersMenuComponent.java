package com.jim.multipos.ui.main_menu.customers_menu.di;

import com.jim.multipos.ui.ActivityScope;
import com.jim.multipos.ui.main_menu.customers_menu.CustomersMenuActivity;

import dagger.Subcomponent;

/**
 * Created by DEV on 08.08.2017.
 */
@ActivityScope
@Subcomponent(modules = {CustomersMenuModule.class})
public interface CustomersMenuComponent {
    void inject(CustomersMenuActivity customersMenuActivity);
}
