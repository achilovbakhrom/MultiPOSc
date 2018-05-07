package com.jim.multipos.ui.settings.security;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class SecurityPresenterModule {
    @Binds
    @PerFragment
    abstract SecurityPresenter provideSecurityPresenter(SecurityPresenterImpl securityPresenter);

}
