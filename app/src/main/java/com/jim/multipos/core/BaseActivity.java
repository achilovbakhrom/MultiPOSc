package com.jim.multipos.core;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.jim.multipos.R;
import com.jim.multipos.utils.managers.LocaleManger;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * Created by user on 26.07.17.
 */

public abstract class BaseActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    @Inject
    @Named(BaseActivityModule.ACTIVITY_FRAGMENT_MANAGER)
    protected FragmentManager activityFragmentManager;
    @Inject
    DispatchingAndroidInjector<Fragment> fragmentInjector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleManger.setLocale(newBase));
    }

    public DispatchingAndroidInjector<Fragment> getFragmentInjector() {
        return fragmentInjector;
    }

    protected final void addFragment(@IdRes int containerViewId, Fragment fragment) {
        if (fragment.isAdded()) return;
        activityFragmentManager.beginTransaction()
                .add(containerViewId, fragment)
                .commit();
    }

    protected final void addFragmentWithBackStack(@IdRes int containerViewId, Fragment fragment) {
        if (fragment.isAdded()) return;
        activityFragmentManager.beginTransaction()
                .add(containerViewId, fragment)
                .addToBackStack(null)
                .commit();
    }

    protected final void replaceFragment(@IdRes int containerViewId, Fragment fragment) {
        if (fragment.isAdded()) return;
        activityFragmentManager.beginTransaction()
                .replace(containerViewId, fragment)
                .commit();
    }

    protected final void replaceFragmentWithBackStack(@IdRes int containerViewId, Fragment fragment) {
        if (fragment.isAdded()) return;
        activityFragmentManager.beginTransaction()
                .replace(containerViewId, fragment)
                .addToBackStack(null)
                .commit();
    }

    protected final void replaceFragmentWithClearTop(@IdRes int containerViewId, Fragment fragment) {
        if (fragment.isAdded()) return;
        while (activityFragmentManager.getBackStackEntryCount() != 0)
            activityFragmentManager.popBackStackImmediate();
        activityFragmentManager.beginTransaction()
                .replace(containerViewId, fragment)
                .commit();
    }

    protected final void addFragmentWithTag(@IdRes int containerViewId, Fragment fragment, String tag) {
        if (fragment.isAdded()) return;

        activityFragmentManager.beginTransaction()
                .add(containerViewId, fragment, tag)
//                .addToBackStack(null)
                .commit();
    }

    protected final void addFragmentWithTagAndBackStack(@IdRes int containerViewId, Fragment fragment, String tag) {
        if (fragment.isAdded()) return;

        activityFragmentManager.beginTransaction()
                .add(containerViewId, fragment, tag)
                .addToBackStack(null)
                .commit();
    }

    protected final void addFragmentWithTagStatic(@IdRes int containerViewId, Fragment fragment, String tag) {
        if (fragment.isAdded()) return;

        activityFragmentManager.beginTransaction()
                .add(containerViewId, fragment, tag)
                .commit();
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentInjector;
    }

    public void onBarcodeScan(String barcode) {
    }

    public final Fragment getCurrentFragment() {
        return activityFragmentManager.findFragmentById(R.id.flMain);

    }
}
