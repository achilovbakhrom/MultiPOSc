package com.jim.multipos.ui.cash_management.view;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.cash_management.presenter.CloseTillSecondStepPresenterModule;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Sirojiddin on 11.01.2018.
 */
@Module(
        includes = CloseTillSecondStepPresenterModule.class
)
public abstract class CloseTillSecondStepFragmentModule {

    @Binds
    @PerFragment
    abstract Fragment provideFragment(CloseTillSecondStepFragment closeTillSecondStepFragment);

    @Binds
    @PerFragment
    abstract CloseTillSecondStepView provideCloseTillSecondStepView(CloseTillSecondStepFragment closeTillSecondStepFragment);
}
