package com.jim.multipos.ui.settings.accounts;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class AccountSettingsPresenterModule {
    @Binds
    @PerFragment
    abstract AccountSettingsPresenter provideAccountSettingsPresenter(AccountSettingsPresenterImpl accountSettingsPresenter);

}
