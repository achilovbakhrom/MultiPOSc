package com.jim.multipos.ui.cash_management.view;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.cash_management.presenter.CashDetailsPresenterModule;
import com.jim.multipos.ui.cash_management.presenter.CashLogPresenterModule;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Sirojiddin on 11.01.2018.
 */
@Module(
        includes = CashDetailsPresenterModule.class
)
public abstract class CashDetailsFragmentModule {

    @Binds
    @PerFragment
    abstract Fragment provideFragment(CashDetailsFragment cashDetailsFragment);

    @Binds
    @PerFragment
    abstract CashDetailsView provideCashDetailsView(CashDetailsFragment cashDetailsFragment);
}
