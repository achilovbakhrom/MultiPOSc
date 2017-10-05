package com.jim.multipos.config.common;

import android.content.Context;

import com.jim.multipos.MultiPosApp;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;

/**
 * Created by bakhrom on 10/3/17.
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<MultiPosApp> {
    }
}
