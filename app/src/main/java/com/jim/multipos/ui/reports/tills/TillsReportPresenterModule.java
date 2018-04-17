package com.jim.multipos.ui.reports.tills;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Sirojiddin on 20.01.2018.
 */
@Module
public abstract class TillsReportPresenterModule {

    @Binds
    @PerFragment
    abstract TillsReportPresenter provideTillsReportPresenter(TillsReportPresenterImpl tillsReportPresenter);
}
