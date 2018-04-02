package com.jim.multipos.ui.reports.di;

import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.core.BaseActivityModule;
import com.jim.multipos.ui.reports.ReportsActivity;
import com.jim.multipos.ui.reports.ReportsActivityPresenter;
import com.jim.multipos.ui.reports.ReportsActivityPresenterImpl;
import com.jim.multipos.ui.reports.ReportsActivityView;
import com.jim.multipos.ui.reports.discount.DiscountReportFragment;
import com.jim.multipos.ui.reports.discount.DiscountReportFragmentModule;
import com.jim.multipos.ui.reports.order_history.OrderHistoryFragment;
import com.jim.multipos.ui.reports.order_history.OrderHistoryFragmentModule;
import com.jim.multipos.ui.reports.sales.SalesReportFragment;
import com.jim.multipos.ui.reports.sales.SalesReportFragmentModule;
import com.jim.multipos.ui.reports.tills.TillsReportFragment;
import com.jim.multipos.ui.reports.tills.TillsReportFragmentModule;

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

    @PerFragment
    @ContributesAndroidInjector(modules = OrderHistoryFragmentModule.class)
    abstract OrderHistoryFragment provideOrderHistoryFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = TillsReportFragmentModule.class)
    abstract TillsReportFragment provideTillsReportFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = DiscountReportFragmentModule.class)
    abstract DiscountReportFragment provideDiscountReportFragment();
}
