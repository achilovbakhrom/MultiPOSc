package com.jim.multipos.ui.reports.hourly_sales;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Sirojiddin on 20.01.2018.
 */
@Module
public abstract class HourlySalesReportPresenterModule {

    @Binds
    @PerFragment
    abstract HourlySalesReportPresenter provideHourlySalesReportPresenter(HourlySalesReportPresenterImpl hourlySalesPresenter);
}
