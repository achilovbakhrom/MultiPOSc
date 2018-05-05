package com.jim.multipos.ui.settings.print;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

@Module(includes = PrintPresenterModule.class)
public abstract class PrintFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment provideFragment(PrintFragment printFragment);

    @Binds
    @PerFragment
    abstract PrintView providPrintFragment(PrintFragment printFragment);
}
