package com.jim.multipos.core;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import javax.inject.Inject;
import javax.inject.Named;


import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;

import dagger.android.support.HasSupportFragmentInjector;

/**
 * Created by user on 26.07.17.
 */

public abstract class BaseActivity extends AppCompatActivity implements HasSupportFragmentInjector{

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentInjector;

    @Inject
    @Named(BaseActivityModule.ACTIVITY_FRAGMENT_MANAGER)
    protected FragmentManager activityFragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public DispatchingAndroidInjector<Fragment> getFragmentInjector() {
        return fragmentInjector;
    }

    protected final void addFragment(@IdRes int containerViewId, Fragment fragment) {
        activityFragmentManager.beginTransaction()
                .add(containerViewId, fragment)
                .commit();
    }

    protected final void replaceFragment(@IdRes int containerViewId, Fragment fragment) {
        activityFragmentManager.beginTransaction()
                .replace(containerViewId, fragment)
                .commit();
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentInjector;
    }

}
