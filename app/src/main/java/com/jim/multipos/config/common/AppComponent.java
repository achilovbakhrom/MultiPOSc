package com.jim.multipos.config.common;

import android.app.Application;

import com.jim.multipos.MultiPosApp;
import com.jim.multipos.data.network.MultiPosApiModule;
import com.jim.multipos.data.network.MultiPosApiService;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

/**
 * Created by bakhrom on 10/3/17.
 */
@Singleton
@Component(modules = {AppModule.class, BaseAppModule.class, ServiceBuilderModule.class, MultiPosApiModule.class})
public interface AppComponent {
    void inject(MultiPosApp app);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
}
