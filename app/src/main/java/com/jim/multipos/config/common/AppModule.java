package com.jim.multipos.config.common;

import android.app.Application;

import com.jim.multipos.MultiPosApp;
import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.ui.signing.SignActivity;
import com.jim.multipos.ui.signing.di.SignActivityModule;

import dagger.Module;
import dagger.android.AndroidInjectionModule;
import dagger.android.ContributesAndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * Created by bakhrom on 10/3/17.
 */

@Module(includes = AndroidSupportInjectionModule.class)
abstract class AppModule {
    abstract Application application(MultiPosApp app);

    @PerActivity
    @ContributesAndroidInjector(modules = SignActivityModule.class)
    abstract SignActivity provideSignActivity();
}
