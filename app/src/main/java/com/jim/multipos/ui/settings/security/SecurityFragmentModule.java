package com.jim.multipos.ui.settings.security;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

@Module(includes = {
        SecurityPresenterModule.class
})
public abstract class SecurityFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment provideFragment(SecurityFragment securityFragment);

    @Binds
    @PerFragment
    abstract SecurityView providSecurityFragment(SecurityFragment securityFragment);
}
