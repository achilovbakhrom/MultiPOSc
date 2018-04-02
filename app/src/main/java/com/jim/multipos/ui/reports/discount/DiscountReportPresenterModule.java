package com.jim.multipos.ui.reports.discount;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.reports.tills.TillsReportPresenter;
import com.jim.multipos.ui.reports.tills.TillsReportPresenterImpl;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Sirojiddin on 20.01.2018.
 */
@Module
public abstract class DiscountReportPresenterModule {

    @Binds
    @PerFragment
    abstract DiscountReportPresenter provideDiscountReportPresenter(DiscountReportPresenterImpl discountReportPresenter);
}
