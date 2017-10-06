package com.jim.multipos.config.common;

import android.app.Application;

import com.jim.multipos.MultiPosApp;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

/**
 * Created by bakhrom on 10/3/17.
 */
@Singleton
@Component(modules = {AppModule.class, BaseAppModule.class})
public interface AppComponent {
    @Component.Builder
    interface Builder  {
        @BindsInstance
        Builder application(Application application);
        AppComponent build();
    }
    void inject(MultiPosApp app);
}
