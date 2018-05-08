package com.jim.multipos.ui.settings.accounts;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.settings.pos_details.PosDetailsPresenter;
import com.jim.multipos.ui.settings.pos_details.PosDetailsPresenterImpl;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class AccountSettingsPresenterModule {
    @Binds
    @PerFragment
    abstract AccountSettingsPresenter provideAccountSettingsPresenter(AccountSettingsPresenterImpl accountSettingsPresenter);

}
