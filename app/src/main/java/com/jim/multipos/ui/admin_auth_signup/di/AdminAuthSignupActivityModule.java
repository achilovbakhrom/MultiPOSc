package com.jim.multipos.ui.admin_auth_signup.di;

import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.core.BaseActivityModule;
import com.jim.multipos.ui.admin_auth_signup.AdminAuthSignupActivity;
import com.jim.multipos.ui.admin_auth_signup.AdminAuthSignupActivityPresenter;
import com.jim.multipos.ui.admin_auth_signup.AdminAuthSignupActivityPresenterImpl;
import com.jim.multipos.ui.admin_auth_signup.AdminAuthSignupActivityView;

import dagger.Binds;
import dagger.Module;

@Module(includes = BaseActivityModule.class)
public abstract class AdminAuthSignupActivityModule {

    @Binds
    @PerActivity
    abstract AppCompatActivity provideLoginPageActivity(AdminAuthSignupActivity adminAuthActivity);

    @Binds
    @PerActivity
    abstract AdminAuthSignupActivityView provideAdminAuthActivityView(AdminAuthSignupActivity adminAuthActivity);

    @Binds
    @PerActivity
    abstract AdminAuthSignupActivityPresenter provideAdminAuthActivityPresenter(AdminAuthSignupActivityPresenterImpl presenter);

}
