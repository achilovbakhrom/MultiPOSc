package com.jim.multipos.ui.admin_auth_signup.fragments.confirmation;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ConfirmationFragmentModule {

    @Binds
    @PerFragment
    abstract Fragment provideFragment(ConfirmationFragment fragment);

    @Binds
    @PerFragment
    abstract ConfirmationView provideGeneralView(ConfirmationFragment fragment);

    @Binds
    @PerFragment
    abstract ConfirmationPresenter provideGeneralPresenter(ConfirmationPresenterImpl presenter);
}
