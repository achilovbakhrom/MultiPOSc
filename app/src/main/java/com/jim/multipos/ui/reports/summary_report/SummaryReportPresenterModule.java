package com.jim.multipos.ui.reports.summary_report;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class SummaryReportPresenterModule {
    @Binds
    @PerFragment
    abstract SummaryReportPresenter provideSummaryReportPresenter(SummaryReportPresenterImpl salesReportPresenter);
}
