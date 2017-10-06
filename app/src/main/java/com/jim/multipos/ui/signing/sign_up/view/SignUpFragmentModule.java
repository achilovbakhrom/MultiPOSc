package com.jim.multipos.ui.signing.sign_up.view;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.signing.sign_up.presenter.SignUpPresenterModule;

import dagger.Binds;
import dagger.Module;

/**
 * Created by bakhrom on 10/6/17.
 */
@Module(includes = {
        SignUpPresenterModule.class
})
public abstract class SignUpFragmentModule {

    @Binds
    @PerFragment
    abstract Fragment provideFragment(SignUpFragment example1Fragment);

    @Binds
    @PerFragment
    abstract SignUpView provideSignUpView(SignUpFragment example1Fragment);

}
