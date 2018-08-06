package com.jim.multipos.ui.reports_admin.di;


import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.core.BaseActivity;
import com.jim.multipos.core.BaseActivityModule;
import com.jim.multipos.ui.reports.ReportsActivity;
import com.jim.multipos.ui.reports.ReportsActivityPresenterImpl;
import com.jim.multipos.ui.reports.ReportsActivityView;
import com.jim.multipos.ui.reports_admin.ReportsAdminActivity;
import com.jim.multipos.ui.reports_admin.ReportsAdminActivityPresenter;
import com.jim.multipos.ui.reports_admin.ReportsAdminActivityPresenterImpl;
import com.jim.multipos.ui.reports_admin.ReportsAdminActivityView;

import dagger.Binds;
import dagger.Module;

@Module(includes = BaseActivityModule.class)
public abstract class ReportsAdminActivityModule {

    @Binds
    @PerActivity
    abstract AppCompatActivity provideReportsAdminActivity(ReportsAdminActivity reportsAdminActivity);

    @Binds
    @PerActivity
    abstract ReportsAdminActivityPresenter provideReportsAdminActivityPresenter(ReportsAdminActivityPresenterImpl presenter);

    @Binds
    @PerActivity
    abstract ReportsAdminActivityView provideReportsAdminActivityView(ReportsAdminActivity reportsActivity);
}
