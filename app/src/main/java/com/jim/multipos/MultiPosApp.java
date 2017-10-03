package com.jim.multipos;

import android.app.Application;
import android.content.Context;

import com.jim.multipos.di.BaseAppComponent;
import com.jim.multipos.di.BaseAppModule;
import com.jim.multipos.di.DaggerBaseAppComponent;


/**
 * Created by DEV on 27.07.2017.
 */

public class MultiPosApp extends Application {

    private BaseAppComponent appComponent;
    private Context context;

    public static MultiPosApp get(Context context) {
        return (MultiPosApp) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        buildComponent();
    }

    public void buildComponent() {
        appComponent = DaggerBaseAppComponent.builder()
                .baseAppModule(new BaseAppModule(this))
                .build();
    }

    public BaseAppComponent getBaseAppComponent() {
        return appComponent;
    }
}
