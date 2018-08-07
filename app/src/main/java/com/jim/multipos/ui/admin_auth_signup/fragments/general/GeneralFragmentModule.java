package com.jim.multipos.ui.admin_auth_signup.fragments.general;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class GeneralFragmentModule {

    @Binds
    @PerFragment
    abstract Fragment provideFragment(GeneralFragment fragment);

    @Binds
    @PerFragment
    abstract GeneralView provideGeneralView(GeneralFragment fragment);

    @Binds
    @PerFragment
    abstract GeneralPresenter provideGeneralPresenter(GeneralPresenterImpl presenter);
}
