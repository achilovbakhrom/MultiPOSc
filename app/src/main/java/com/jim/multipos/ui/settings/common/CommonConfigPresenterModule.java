package com.jim.multipos.ui.settings.common;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class CommonConfigPresenterModule {
    @Binds
    @PerFragment
    abstract CommonConfigPresenter provideCommonConfigPresenter(CommonConfigPresenterImpl commonConfigPresenter);

}
