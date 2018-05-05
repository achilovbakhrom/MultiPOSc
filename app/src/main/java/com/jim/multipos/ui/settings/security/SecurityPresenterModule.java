package com.jim.multipos.ui.settings.security;

import com.jim.multipos.config.scope.PerActivity;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class SecurityPresenterModule {
    @Binds
    @PerActivity
    abstract SecurityPresenter provideSecurityPresenter(SecurityPresenterImpl securityPresenter);

}
