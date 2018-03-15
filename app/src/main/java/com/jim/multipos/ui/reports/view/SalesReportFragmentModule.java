package com.jim.multipos.ui.reports.view;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.reports.presenter.SalesReportPresenterModule;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Sirojiddin on 20.01.2018.
 */
@Module(
        includes = SalesReportPresenterModule.class
)
public abstract class SalesReportFragmentModule {

    @Binds
    @PerFragment
    abstract Fragment provideFragment(SalesReportFragment salesReportFragment);

    @Binds
    @PerFragment
    abstract SalesReportView provideSalesReportView(SalesReportFragment salesReportFragment);

}
