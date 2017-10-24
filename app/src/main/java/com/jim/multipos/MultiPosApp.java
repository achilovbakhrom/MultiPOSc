package com.jim.multipos;

import android.app.Activity;
import android.app.Application;

import com.jim.multipos.config.common.DaggerAppComponent;
import com.jim.multipos.di.BaseAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;


/**
 * Created by DEV on 27.07.2017.
 */

public class MultiPosApp extends Application implements HasActivityInjector{

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerAppComponent.builder().application(this).build().inject(this);
    }

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
