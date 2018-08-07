package com.jim.multipos.ui.admin_auth_signup.fragments.info;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class InfoFragmentModule {

    @Binds
    @PerFragment
    abstract Fragment provideFragment(InfoFragment fragment);

    @Binds
    @PerFragment
    abstract InfoView provideGeneralView(InfoFragment fragment);

    @Binds
    @PerFragment
    abstract InfoPresenter provideGeneralPresenter(InfoPresenterImpl presenter);
}
