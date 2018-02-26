package com.jim.multipos.ui.cash_management.view;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.cash_management.presenter.CloseTillDialogFragmentPresenterModule;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Sirojiddin on 12.01.2018.
 */
@Module(
        includes = CloseTillDialogFragmentPresenterModule.class
)
public abstract class CloseTillDialogFragmentModule {

    @Binds
    @PerFragment
    abstract Fragment provideFragement(CloseTillDialogFragment closeTillDialogFragment);

    @Binds
    @PerFragment
    abstract CloseTillDialogFragmentView provideCloseTillDialogView(CloseTillDialogFragment closeTillDialogFragment);
}
