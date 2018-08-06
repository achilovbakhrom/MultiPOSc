package com.jim.multipos.ui.admin_auth_signin.di;

import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.core.BaseActivityModule;
import com.jim.multipos.ui.admin_auth_signin.AdminAuthSigninActivity;
import com.jim.multipos.ui.admin_auth_signin.AdminAuthSigninActivityPresenter;
import com.jim.multipos.ui.admin_auth_signin.AdminAuthSigninActivityPresenterImpl;
import com.jim.multipos.ui.admin_auth_signin.AdminAuthSigninActivityView;

import dagger.Binds;
import dagger.Module;

@Module(includes = BaseActivityModule.class)
public abstract class AdminAuthSigninActivityModule {

    @Binds
    @PerActivity
    abstract AppCompatActivity provideLoginPageActivity(AdminAuthSigninActivity adminAuthActivity);

    @Binds
    @PerActivity
    abstract AdminAuthSigninActivityView provideAdminAuthActivityView(AdminAuthSigninActivity adminAuthActivity);

    @Binds
    @PerActivity
    abstract AdminAuthSigninActivityPresenter provideAdminAuthActivityPresenter(AdminAuthSigninActivityPresenterImpl presenter);
}
