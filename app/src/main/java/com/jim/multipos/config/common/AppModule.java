package com.jim.multipos.config.common;

import android.app.Application;

import com.jim.multipos.MultiPosApp;

import dagger.Module;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * Created by bakhrom on 10/3/17.
 */

@Module(includes = AndroidSupportInjectionModule.class)
abstract class AppModule {
    abstract Application application(MultiPosApp app);
}
