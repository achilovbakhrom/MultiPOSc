package com.jim.multipos.ui.signing.di;


import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.core.BaseActivityModule;
import com.jim.multipos.ui.signing.SignActivity;
import com.jim.multipos.ui.signing.sign_up.view.RegistrationConfirmFragment;
import com.jim.multipos.ui.signing.sign_up.view.RegistrationConfirmFragmentModule;
import com.jim.multipos.ui.signing.sign_up.view.SignUpFragment;
import com.jim.multipos.ui.signing.sign_up.view.SignUpFragmentModule;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by DEV on 27.07.2017.
 */
@Module(includes = BaseActivityModule.class)
public abstract class SignActivityModule {

    @Binds
    @PerActivity
    abstract AppCompatActivity provideSignActivity(SignActivity signActivity);

    @PerFragment
    @ContributesAndroidInjector(modules = SignUpFragmentModule.class)
    abstract SignUpFragment provideSugnUpFragmentInjector();

    @PerFragment
    @ContributesAndroidInjector(modules = RegistrationConfirmFragmentModule.class)
    abstract RegistrationConfirmFragment provideRegistrationConfirmFragmentInjector();

}
