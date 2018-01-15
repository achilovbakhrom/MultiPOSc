package com.jim.multipos.ui.cash_management.view;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.cash_management.presenter.CloseTillFirstStepPresenterModule;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Sirojiddin on 12.01.2018.
 */
@Module(
        includes = CloseTillFirstStepPresenterModule.class
)
public abstract class CloseTillFirstStepFragmentModule {

    @Binds
    @PerFragment
    abstract Fragment provideFragement(CloseTillFirstStepFragment closeTillFirstStepFragment);

    @Binds
    @PerFragment
    abstract CloseTillFirstStepView provideCloseTillFirstStepView(CloseTillFirstStepFragment closeTillFirstStepFragment);
}
