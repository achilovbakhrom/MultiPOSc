package com.jim.multipos.ui.settings.accounts;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

@Module(includes = AccountSettingsPresenterModule.class)
public abstract class AccountSettingsFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment provideFragment(AccountSettingsFragment fragment);

    @Binds
    @PerFragment
    abstract AccountSettingsView provideAccountSettingsView(AccountSettingsFragment fragment);
}
