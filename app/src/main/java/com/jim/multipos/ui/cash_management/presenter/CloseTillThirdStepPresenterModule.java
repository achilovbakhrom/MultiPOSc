package com.jim.multipos.ui.cash_management.presenter;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Sirojiddin on 13.01.2018.
 */
@Module
public abstract class CloseTillThirdStepPresenterModule {

    @Binds
    @PerFragment
    abstract CloseTillThirdStepPresenter closeTillThirdStepPresenter(CloseTillThirdStepPresenterImpl closeTillThirdStepPresenter);
}
