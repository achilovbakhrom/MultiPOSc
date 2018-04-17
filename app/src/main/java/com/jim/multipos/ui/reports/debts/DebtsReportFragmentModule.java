package com.jim.multipos.ui.reports.debts;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Sirojiddin on 20.01.2018.
 */
@Module(
        includes = DebtsReportPresenterModule.class
)
public abstract class DebtsReportFragmentModule {

    @Binds
    @PerFragment
    abstract Fragment provideFragment(DebtsReportFragment debtsReportFragment);

    @Binds
    @PerFragment
    abstract DebtsReportView provideDebtsReportView(DebtsReportFragment debtsReportFragment);

}
