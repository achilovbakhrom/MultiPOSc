package com.jim.multipos.ui.cash_management.presenter;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Sirojiddin on 12.01.2018.
 */
@Module
public abstract class CloseTillFirstStepPresenterModule {
    @Binds
    @PerFragment
    abstract CloseTillFirstStepPresenter provideCloseTillFirstStepPresenter(CloseTillFirstStepPresenterImpl closeTillFirstStepPresenter);
}
