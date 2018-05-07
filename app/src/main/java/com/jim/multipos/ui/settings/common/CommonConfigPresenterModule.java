package com.jim.multipos.ui.settings.common;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.settings.security.SecurityPresenter;
import com.jim.multipos.ui.settings.security.SecurityPresenterImpl;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class CommonConfigPresenterModule {
    @Binds
    @PerFragment
    abstract CommonConfigPresenter provideCommonConfigPresenter(CommonConfigPresenterImpl commonConfigPresenter);

}
