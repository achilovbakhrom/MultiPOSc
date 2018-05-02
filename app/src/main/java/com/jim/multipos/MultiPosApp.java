package com.jim.multipos;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Intent;
import android.support.multidex.MultiDexApplication;

import com.jim.multipos.config.common.DaggerAppComponent;
import com.jim.multipos.di.BaseAppComponent;
import com.jim.multipos.utils.usb_barcode.USBService;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasServiceInjector;


/**
 * Created by DEV on 27.07.2017.
 */

public class MultiPosApp extends MultiDexApplication implements HasActivityInjector,HasServiceInjector{

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerAppComponent.builder().application(this).build().inject(this);
        startService(new Intent(this, USBService.class));
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
}
