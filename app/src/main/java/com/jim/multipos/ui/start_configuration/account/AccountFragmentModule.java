package com.jim.multipos.ui.start_configuration.account;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

@Module(includes = AccountPresenterModule.class)
public abstract class AccountFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment provideFragment(AccountFragment fragment);

    @Binds
    @PerFragment
    abstract AccountView provideAccountView(AccountFragment fragment);
}
