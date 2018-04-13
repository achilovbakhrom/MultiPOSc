package com.jim.multipos.ui.reports.inventory;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class InventoryReportPresenterModule {

    @Binds
    @PerFragment
    abstract InventoryReportPresenter provideInventoryReportPresenter(InventoryReportPresenterImpl inventoryReportPresenter);
}
