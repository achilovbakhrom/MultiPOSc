package com.jim.multipos.ui.cash_management.view;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.cash_management.presenter.CloseTillThirdStepPresenterModule;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Sirojiddin on 13.01.2018.
 */
@Module(
        includes = CloseTillThirdStepPresenterModule.class
)
public abstract class CloseTillThirdStepFragmentModule {

    @Binds
    @PerFragment
    abstract Fragment provideFragment(CloseTillThirdStepFragment closeTillThirdStepFragment);

    @Binds
    @PerFragment
    abstract CloseTillThirdStepView provideCloseTillThirdStepView(CloseTillThirdStepFragment closeTillThirdStepFragment);
}
