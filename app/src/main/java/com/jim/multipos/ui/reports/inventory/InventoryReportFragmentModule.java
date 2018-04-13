package com.jim.multipos.ui.reports.inventory;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.reports.hourly_sales.HourlySalesReportFragment;
import com.jim.multipos.ui.reports.hourly_sales.HourlySalesReportPresenterModule;
import com.jim.multipos.ui.reports.hourly_sales.HourlySalesReportView;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Sirojiddin on 20.01.2018.
 */
@Module(
        includes = InventoryReportPresenterModule.class
)
public abstract class InventoryReportFragmentModule {

    @Binds
    @PerFragment
    abstract Fragment provideFragment(InventoryReportFragment inventoryReportFragment);

    @Binds
    @PerFragment
    abstract InventoryReportView provideInventoryReportView(InventoryReportFragment inventoryReportFragment);

}
