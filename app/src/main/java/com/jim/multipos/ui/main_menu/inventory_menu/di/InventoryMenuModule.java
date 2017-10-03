package com.jim.multipos.ui.main_menu.inventory_menu.di;

import com.jim.multipos.ui.ActivityScope;
import com.jim.multipos.ui.main_menu.customers_menu.CustomersMenuActivity;
import com.jim.multipos.ui.main_menu.customers_menu.presenters.CustomerMenuPresenterImpl;
import com.jim.multipos.ui.main_menu.customers_menu.presenters.CustomersMenuPresenter;
import com.jim.multipos.ui.main_menu.inventory_menu.InventoryMenuActivity;
import com.jim.multipos.ui.main_menu.inventory_menu.presenters.InventoryMenuPresenter;
import com.jim.multipos.ui.main_menu.inventory_menu.presenters.InventoryMenuPresenterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by DEV on 08.08.2017.
 */
@Module
public class InventoryMenuModule {
    private InventoryMenuActivity activity;

    public InventoryMenuModule(InventoryMenuActivity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    public InventoryMenuActivity getActivity() {
        return activity;
    }


    @Provides
    @ActivityScope
    public InventoryMenuPresenter getMenuInventoryPresenter() {
        return new InventoryMenuPresenterImpl();
    }
}
