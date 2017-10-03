package com.jim.multipos.ui.login.di;

import com.jim.multipos.ui.ActivityScope;
import com.jim.multipos.ui.login.LoginPageActivity;
import com.jim.multipos.ui.login.LoginPageImpl;
import com.jim.multipos.ui.login.LoginPagePresenter;
import com.jim.multipos.utils.managers.PosFragmentManager;

import dagger.Module;
import dagger.Provides;

/**
 * Created by DEV on 04.08.2017.
 */
@Module
public class LoginPageModule {
    private LoginPageActivity activity;

    public LoginPageModule(LoginPageActivity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    public LoginPageActivity getActivity() {
        return activity;
    }

    @Provides
    @ActivityScope
    public PosFragmentManager getFragmentManager(LoginPageActivity activity) {
        return new PosFragmentManager(activity);
    }

    @Provides
    @ActivityScope
    public LoginPagePresenter getRegistrationPresenter() {
        return new LoginPageImpl();
    }
}
