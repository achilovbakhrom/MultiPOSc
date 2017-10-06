package com.jim.multipos.ui.signing.sign_up.presenter;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

/**
 * Created by bakhrom on 10/6/17.
 */

@Module
public abstract class SignUpPresenterModule {
    @Binds
    @PerFragment
    abstract SignUpPresenter provideSignUpPresenter(SignUpPresenterImpl signUpPresenterImpl);
}
