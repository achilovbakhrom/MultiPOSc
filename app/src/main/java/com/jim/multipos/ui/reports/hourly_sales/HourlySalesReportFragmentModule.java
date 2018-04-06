package com.jim.multipos.ui.reports.hourly_sales;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.reports.tills.TillsReportFragment;
import com.jim.multipos.ui.reports.tills.TillsReportView;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Sirojiddin on 20.01.2018.
 */
@Module(
        includes = HourlySalesReportPresenterModule.class
)
public abstract class HourlySalesReportFragmentModule {

    @Binds
    @PerFragment
    abstract Fragment provideFragment(HourlySalesReportFragment hourlySalesReportFragment);

    @Binds
    @PerFragment
    abstract HourlySalesReportView provideHourlySalesReportView(HourlySalesReportFragment hourlySalesReportFragment);

}
