package com.jim.multipos.ui.start_configuration.basics;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class BasicsPresenterModule {
    @Binds
    @PerFragment
    abstract BasicsPresenter provideBasicsPresenter(BasicsPresenterImpl basicsPresenter);

}
