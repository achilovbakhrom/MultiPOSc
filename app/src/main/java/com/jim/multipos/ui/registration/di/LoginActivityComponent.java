package com.jim.multipos.ui.registration.di;

import com.jim.multipos.di.BaseAppComponent;
import com.jim.multipos.ui.registration.LoginActivity;
import com.jim.multipos.ui.ActivityScope;
import com.jim.multipos.ui.registration.fragments.LoginDetailsFragment;
import com.jim.multipos.ui.registration.fragments.RegistrationConfirmFragment;
import com.jim.multipos.ui.registration.fragments.RegistrationFragment;



import dagger.Component;
import dagger.Subcomponent;

/**
 * Created by DEV on 27.07.2017.
 */
@ActivityScope
@Subcomponent(modules = {LoginActivityModule.class})
public interface LoginActivityComponent {
    void inject(LoginActivity loginActivity);
    void inject(LoginDetailsFragment loginDetailsFragment);
    void inject(RegistrationConfirmFragment confirmFragment);
    void inject(RegistrationFragment registrationFragment);
}
