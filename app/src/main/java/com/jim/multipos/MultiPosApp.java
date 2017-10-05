package com.jim.multipos;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.jim.multipos.di.BaseAppComponent;
import com.jim.multipos.di.BaseAppModule;


import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;


/**
 * Created by DEV on 27.07.2017.
 */

public class MultiPosApp extends Application implements HasActivityInjector{

//    private BaseAppComponent appComponent;
    private Context context;

    public static MultiPosApp get(Context context) {
        return (MultiPosApp) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

//        buildComponent();
    }

//    public void buildComponent() {
//        appComponent = DaggerBaseAppComponent.builder()
//                .baseAppModule(new BaseAppModule(this))
//                .build();
//
//    }

    public BaseAppComponent getBaseAppComponent() {
        return null;
    }

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}
