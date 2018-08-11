package com.jim.multipos.ui.admin_main_page.fragments.dashboard.di;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.admin_main_page.fragments.dashboard.DashboardMainFragment;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class DashBoardMainModule {

    @Binds
    @PerFragment
    abstract Fragment provideFragment(DashboardMainFragment fragment);
}
