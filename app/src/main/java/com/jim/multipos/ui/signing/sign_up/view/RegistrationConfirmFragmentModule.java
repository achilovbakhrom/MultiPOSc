package com.jim.multipos.ui.signing.sign_up.view;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.signing.sign_up.presenter.RegistrationConfirmPresenterModule;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Sirojiddin on 12.10.2017.
 */
@Module(includes = {
        RegistrationConfirmPresenterModule.class
})
public abstract class RegistrationConfirmFragmentModule {

    @Binds
    @PerFragment
    abstract Fragment provideFragment(RegistrationConfirmFragment confirmFragment);

    @Binds
    @PerFragment
    abstract RegistrationConfirmView provideRegistrationConfirmFragmentView (RegistrationConfirmFragment confirmFragment);
}
