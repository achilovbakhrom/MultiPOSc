package com.jim.multipos.ui.signing.sign_up.presenter;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Sirojiddin on 12.10.2017.
 */
@Module
public abstract class RegistrationConfirmPresenterModule {
    @Binds
    @PerFragment
    abstract RegistrationConfirmPresenter provideRegistrationConfirmPresenter(RegistrationConfirmPresenterImpl registrationConfirmPresenter);
}
