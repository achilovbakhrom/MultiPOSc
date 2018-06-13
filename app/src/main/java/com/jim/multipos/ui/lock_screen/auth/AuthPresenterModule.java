package com.jim.multipos.ui.lock_screen.auth;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class AuthPresenterModule {
    @Binds
    @PerFragment
    abstract AuthPresenter provideAuthPresenter(AuthPresenterImpl authPresenter);
}
