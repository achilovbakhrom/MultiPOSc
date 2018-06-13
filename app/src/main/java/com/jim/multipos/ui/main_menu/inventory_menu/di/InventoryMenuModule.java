package com.jim.multipos.ui.main_menu.inventory_menu.di;

import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.core.BaseActivityModule;
import com.jim.multipos.ui.main_menu.inventory_menu.InventoryMenuActivity;
import com.jim.multipos.ui.main_menu.inventory_menu.InventoryMenuView;
import com.jim.multipos.ui.main_menu.inventory_menu.presenters.InventoryMenuPresenter;
import com.jim.multipos.ui.main_menu.inventory_menu.presenters.InventoryMenuPresenterImpl;

import dagger.Binds;
import dagger.Module;

/**
 * Created by bakhrom on 10/6/17.
 */

@Module(includes = BaseActivityModule.class)
public abstract class InventoryMenuModule {
    @Binds
    @PerActivity
    abstract AppCompatActivity provideInventoryMenuActivity(InventoryMenuActivity inventoryMenuActivity);

    @Binds
    @PerActivity
    abstract InventoryMenuPresenter provideInventoryMenuPresenter(InventoryMenuPresenterImpl inventoryMenuPresenter);

    @Binds
    @PerActivity
    abstract InventoryMenuView provideInventoryMenuActivityView(InventoryMenuActivity inventoryMenuActivity);

}
