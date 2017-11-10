package com.jim.multipos.ui.inventory.fragments;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.inventory.presenter.InventoryPresenter;
import com.jim.multipos.ui.inventory.presenter.InventoryPresenterImpl;

import dagger.Binds;
import dagger.Module;

/**
 * Created by developer on 09.11.2017.
 */
@Module
public abstract class InventoryPresenterModule {
    @Binds
    @PerFragment
    abstract InventoryPresenter provideInventoryPresenter(InventoryPresenterImpl inventoryPresenter);
}
