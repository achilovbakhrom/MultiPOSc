package com.jim.multipos.ui.reports.di;

import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.core.BaseActivityModule;
import com.jim.multipos.ui.reports.ReportsActivity;
import com.jim.multipos.ui.reports.ReportsActivityPresenter;
import com.jim.multipos.ui.reports.ReportsActivityPresenterImpl;
import com.jim.multipos.ui.reports.ReportsActivityView;
import com.jim.multipos.ui.reports.view.SalesReportFragment;
import com.jim.multipos.ui.reports.view.SalesReportFragmentModule;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Sirojiddin on 12.03.2018.
 */
@Module(includes = BaseActivityModule.class)
public abstract class ReportsActivityModule {

    @Binds
    @PerActivity
    abstract AppCompatActivity provideReportsActivity(ReportsActivity reportsActivity);

    @Binds
    @PerActivity
    abstract ReportsActivityPresenter provideReportsActivityPresenter(ReportsActivityPresenterImpl presenter);

    @Binds
    @PerActivity
    abstract ReportsActivityView provideReportsActivityView(ReportsActivity reportsActivity);

    @PerFragment
    @ContributesAndroidInjector(modules = SalesReportFragmentModule.class)
    abstract SalesReportFragment provideSalesReportFragmentInjector();
}
