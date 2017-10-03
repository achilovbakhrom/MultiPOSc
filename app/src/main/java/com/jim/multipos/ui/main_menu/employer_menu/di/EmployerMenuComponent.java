package com.jim.multipos.ui.main_menu.employer_menu.di;

import com.jim.multipos.ui.ActivityScope;
import com.jim.multipos.ui.main_menu.employer_menu.EmployerMenuActivity;
import com.jim.multipos.ui.main_menu.inventory_menu.InventoryMenuActivity;
import com.jim.multipos.ui.main_menu.inventory_menu.di.InventoryMenuModule;

import dagger.Subcomponent;

/**
 * Created by DEV on 08.08.2017.
 */
@ActivityScope
@Subcomponent(modules = {EmployerMenuModule.class})
public interface EmployerMenuComponent {
    void inject(EmployerMenuActivity employerMenuActivity);
}
