package com.jim.multipos.ui.reports.summary_report;

import android.support.v4.app.Fragment;
import com.jim.multipos.config.scope.PerFragment;
import dagger.Binds;
import dagger.Module;

@Module(
        includes = SummaryReportPresenterModule.class
)
public abstract class SummaryReportFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment provideFragment(SummaryReportFragment summaryReportFragment);

    @Binds
    @PerFragment
    abstract SummaryReportView provideSummaryReportView(SummaryReportFragment summaryReportFragment);
}
