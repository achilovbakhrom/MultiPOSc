package com.jim.multipos.ui.settings.accounts;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.settings.pos_details.PosDetailsFragment;
import com.jim.multipos.ui.settings.pos_details.PosDetailsPresenterModule;
import com.jim.multipos.ui.settings.pos_details.PosDetailsView;

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
