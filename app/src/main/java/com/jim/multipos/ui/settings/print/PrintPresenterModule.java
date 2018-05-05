package com.jim.multipos.ui.settings.print;

import com.jim.multipos.config.scope.PerActivity;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class PrintPresenterModule {
    @Binds
    @PerActivity
    abstract PrintPresenter providePrintPresenter(PrintPresenterImpl printPresenter);

}
