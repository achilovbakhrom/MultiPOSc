package com.jim.multipos.ui.main_menu.inventory_menu.di;

import com.jim.multipos.ui.ActivityScope;
import com.jim.multipos.ui.main_menu.customers_menu.CustomersMenuActivity;
import com.jim.multipos.ui.main_menu.customers_menu.di.CustomersMenuModule;
import com.jim.multipos.ui.main_menu.inventory_menu.InventoryMenuActivity;

import dagger.Subcomponent;

/**
 * Created by DEV on 08.08.2017.
 */
@ActivityScope
@Subcomponent(modules = {InventoryMenuModule.class})
public interface InventoryMenuComponent {
    void inject(InventoryMenuActivity inventoryMenuActivity);
}
