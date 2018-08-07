package com.jim.multipos.ui.admin_auth_signup.di;

import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.core.BaseActivityModule;
import com.jim.multipos.ui.admin_auth_signup.AdminAuthSignupActivity;
import com.jim.multipos.ui.admin_auth_signup.AdminAuthSignupActivityPresenter;
import com.jim.multipos.ui.admin_auth_signup.AdminAuthSignupActivityPresenterImpl;
import com.jim.multipos.ui.admin_auth_signup.AdminAuthSignupActivityView;
import com.jim.multipos.ui.admin_auth_signup.fragments.confirmation.ConfirmationFragment;
import com.jim.multipos.ui.admin_auth_signup.fragments.confirmation.ConfirmationFragmentModule;
import com.jim.multipos.ui.admin_auth_signup.fragments.general.GeneralFragment;
import com.jim.multipos.ui.admin_auth_signup.fragments.general.GeneralFragmentModule;
import com.jim.multipos.ui.admin_auth_signup.fragments.info.InfoFragment;
import com.jim.multipos.ui.admin_auth_signup.fragments.info.InfoFragmentModule;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

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

    @PerFragment
    @ContributesAndroidInjector(modules = GeneralFragmentModule.class)
    abstract GeneralFragment provideGeneralFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = InfoFragmentModule.class)
    abstract InfoFragment provideInfoFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = ConfirmationFragmentModule.class)
    abstract ConfirmationFragment provideConfirmationFragment();
}
