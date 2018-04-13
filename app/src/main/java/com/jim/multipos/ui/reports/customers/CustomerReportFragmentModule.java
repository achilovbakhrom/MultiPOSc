package com.jim.multipos.ui.reports.customers;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.reports.debts.DebtsReportFragment;
import com.jim.multipos.ui.reports.debts.DebtsReportPresenterModule;
import com.jim.multipos.ui.reports.debts.DebtsReportView;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Sirojiddin on 20.01.2018.
 */
@Module(
        includes = CustomerReportPresenterModule.class
)
public abstract class CustomerReportFragmentModule {

    @Binds
    @PerFragment
    abstract Fragment provideFragment(CustomerReportFragment customerReportFragment);

    @Binds
    @PerFragment
    abstract CustomerReportView provideCustomerReportView(CustomerReportFragment customerReportFragment);

}
