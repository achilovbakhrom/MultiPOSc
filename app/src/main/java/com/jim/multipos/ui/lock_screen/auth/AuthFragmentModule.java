package com.jim.multipos.ui.lock_screen.auth;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

@Module(
        includes = AuthPresenterModule.class
)
public abstract  class AuthFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment provideFragment(AuthFragment authFragment);

    @Binds
    @PerFragment
    abstract AuthView provideAuthView(AuthFragment authFragment);
}
