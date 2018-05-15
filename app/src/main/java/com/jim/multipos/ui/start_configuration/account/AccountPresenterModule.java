package com.jim.multipos.ui.start_configuration.account;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class AccountPresenterModule {
    @Binds
    @PerFragment
    abstract AccountPresenter provideAccountPresenter(AccountPresenterImpl presenter);

}
