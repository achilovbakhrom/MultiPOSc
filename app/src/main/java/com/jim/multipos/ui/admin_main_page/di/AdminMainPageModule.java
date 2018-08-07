package com.jim.multipos.ui.admin_main_page.di;

import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.core.BaseActivityModule;
import com.jim.multipos.ui.admin_main_page.AdminMainPageActivity;

import dagger.Binds;
import dagger.Module;

@Module(includes = BaseActivityModule.class)
public abstract class AdminMainPageModule {

    @Binds
    @PerActivity
    abstract AppCompatActivity provideAdminMainPageActivity(AdminMainPageActivity adminAuthActivity);
}
