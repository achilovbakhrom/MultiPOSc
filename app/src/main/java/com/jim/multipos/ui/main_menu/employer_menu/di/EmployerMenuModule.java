package com.jim.multipos.ui.main_menu.employer_menu.di;

import com.jim.multipos.ui.ActivityScope;
import com.jim.multipos.ui.main_menu.employer_menu.EmployerMenuActivity;
import com.jim.multipos.ui.main_menu.employer_menu.presenters.EmployerMenuPresenter;
import com.jim.multipos.ui.main_menu.employer_menu.presenters.EmployerMenuPresenterImpl;
import com.jim.multipos.ui.main_menu.inventory_menu.InventoryMenuActivity;
import com.jim.multipos.ui.main_menu.inventory_menu.presenters.InventoryMenuPresenter;
import com.jim.multipos.ui.main_menu.inventory_menu.presenters.InventoryMenuPresenterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by DEV on 08.08.2017.
 */
@Module
public class EmployerMenuModule {
    private EmployerMenuActivity activity;

    public EmployerMenuModule(EmployerMenuActivity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    public EmployerMenuActivity getActivity() {
        return activity;
    }


    @Provides
    @ActivityScope
    public EmployerMenuPresenter getMenuEmployerPresenter() {
        return new EmployerMenuPresenterImpl();
    }
}
