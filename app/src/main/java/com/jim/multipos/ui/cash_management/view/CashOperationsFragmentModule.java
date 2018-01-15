package com.jim.multipos.ui.cash_management.view;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.cash_management.presenter.CashOperationsPresenterModule;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Sirojiddin on 11.01.2018.
 */
@Module(
        includes = CashOperationsPresenterModule.class
)
public abstract class CashOperationsFragmentModule {

    @Binds
    @PerFragment
    abstract Fragment provideFragment(CashOperationsFragment cashOperationsFragment);

    @Binds
    @PerFragment
    abstract CashOperationsView provideCashOperationsView(CashOperationsFragment cashOperationsFragment);
}
