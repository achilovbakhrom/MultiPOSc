package com.jim.multipos.ui.cash_management.presenter;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Sirojiddin on 11.01.2018.
 */
@Module
public abstract class CashLogPresenterModule {
    @Binds
    @PerFragment
    abstract CashLogPresenter provideCashLogPresenter(CashLogPresenterImpl cashLogPresenter);
}
