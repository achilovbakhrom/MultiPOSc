package com.jim.multipos.ui.reports.tills;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.reports.sales.SalesReportPresenterModule;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Sirojiddin on 20.01.2018.
 */
@Module(
        includes = TillsReportPresenterModule.class
)
public abstract class TillsReportFragmentModule {

    @Binds
    @PerFragment
    abstract Fragment provideFragment(TillsReportFragment tillsReportFragment);

    @Binds
    @PerFragment
    abstract TillsReportView provideTillsReportView(TillsReportFragment tillsReportFragment);

}
