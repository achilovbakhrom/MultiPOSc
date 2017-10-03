package com.jim.multipos.ui.login.di;

import com.jim.multipos.ui.ActivityScope;
import com.jim.multipos.ui.login.LoginPageActivity;

import dagger.Subcomponent;

/**
 * Created by DEV on 04.08.2017.
 */
@ActivityScope
@Subcomponent(modules = LoginPageModule.class)
public interface LoginPageComponent {
    void inject(LoginPageActivity loginPageActivity);
}
