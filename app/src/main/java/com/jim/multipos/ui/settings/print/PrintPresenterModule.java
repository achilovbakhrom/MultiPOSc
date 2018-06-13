package com.jim.multipos.ui.settings.print;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class PrintPresenterModule {
    @Binds
    @PerFragment
    abstract PrintPresenter providePrintPresenter(PrintPresenterImpl printPresenter);

}
