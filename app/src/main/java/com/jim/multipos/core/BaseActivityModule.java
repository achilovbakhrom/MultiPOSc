package com.jim.multipos.core;



import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerActivity;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * Provides base activity dependencies. This must be included in all activity modules, which must
 * provide a concrete implementation of {@link AppCompatActivity}.
 */
@Module
public abstract class BaseActivityModule {

    static final String ACTIVITY_FRAGMENT_MANAGER = "BaseActivityModule.activityFragmentManager";

    @Binds
    @PerActivity
    abstract Context activityContext(AppCompatActivity activity);

    @Provides
    @Named(ACTIVITY_FRAGMENT_MANAGER)
    @PerActivity
    static FragmentManager activityFragmentManager(AppCompatActivity activity) {
        return activity.getSupportFragmentManager();
    }
}