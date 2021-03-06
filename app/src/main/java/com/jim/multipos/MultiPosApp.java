package com.jim.multipos;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.jim.multipos.config.common.DaggerAppComponent;
import com.jim.multipos.di.BaseAppComponent;
import com.jim.multipos.utils.managers.LocaleManger;
import com.squareup.leakcanary.LeakCanary;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasServiceInjector;


/**
 * Created by DEV on 27.07.2017.
 */

public class MultiPosApp extends MultiDexApplication implements HasActivityInjector, HasServiceInjector {

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerAppComponent.builder().application(this).build().inject(this);
        LeakCanary.install(this);
    }

    public BaseAppComponent getBaseAppComponent() {
        return null;
    }

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;
    @Inject
    DispatchingAndroidInjector<Service> dispatchingServiceInjector;

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    public AndroidInjector<Service> serviceInjector() {
        return dispatchingServiceInjector;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManger.setLocale(base));
    }

}
