package com.jim.multipos.ui.cash_management.view;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.cash_management.presenter.CashLogPresenterModule;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Sirojiddin on 11.01.2018.
 */
@Module(
        includes = CashLogPresenterModule.class
)
public abstract class CashLogFragmentModule {

    @Binds
    @PerFragment
    abstract Fragment provideFragment(CashLogFragment cashLogFragment);

    @Binds
    @PerFragment
    abstract CashLogView provideCashLogView(CashLogFragment cashLogFragment);
}
