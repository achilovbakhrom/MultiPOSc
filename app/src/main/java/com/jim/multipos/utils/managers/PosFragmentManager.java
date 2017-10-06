package com.jim.multipos.utils.managers;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

/**
 * Created by DEV on 26.07.2017.
 */

public class PosFragmentManager {
    private AppCompatActivity activity;
    private FragmentManager fragmentManager;

    @Inject
    public PosFragmentManager(AppCompatActivity activity) {
        this.activity = activity;
        fragmentManager = activity.getSupportFragmentManager();
    }

    public void displayFragment(Fragment fragment, int id) {
        fragmentManager
                .beginTransaction()
                .addToBackStack(null)
                .add(id, fragment)
                .commit();
    }

    public void replaceFragment(Fragment fragment, int id) {
        fragmentManager
                .beginTransaction()
                .addToBackStack(null)
                .replace(id, fragment)
                .commit();
    }

    public void displayFragmentWithoutBackStack(Fragment fragment, int id) {
        fragmentManager
                .beginTransaction()
                .replace(id, fragment)
                .commit();
    }

    public void replaceFragmentWithTag(Fragment fragment, int id, String tag) {
        fragmentManager
                .beginTransaction()
                .replace(id, fragment, tag)
                .commit();
    }

    public void popBackStack() {
        fragmentManager.popBackStack();
    }

    public Fragment findFragmentByTag(String tag) {
        return fragmentManager.findFragmentByTag(tag);
    }

    public Fragment getCurrentFragment() {
        String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
        Fragment currentFragment = fragmentManager.findFragmentByTag(fragmentTag);

        return currentFragment;
    }
}
