package com.jim.multipos.ui.settings.common;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

@Module(includes = {
        CommonConfigPresenterModule.class
})
public abstract class CommonConfigFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment provideFragment(CommonConfigFragment commonConfigFragment);

    @Binds
    @PerFragment
    abstract CommonConfigView providCommonConfigFragment(CommonConfigFragment commonConfigFragment);
}
