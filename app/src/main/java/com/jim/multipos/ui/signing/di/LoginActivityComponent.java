package com.jim.multipos.ui.signing.di;

import com.jim.multipos.ui.signing.SignActivity;
import com.jim.multipos.ui.ActivityScope;
import com.jim.multipos.ui.signing.sign_in.LoginDetailsFragment;
import com.jim.multipos.ui.signing.sign_up.RegistrationConfirmFragment;
import com.jim.multipos.ui.signing.sign_up.RegistrationFragment;


import dagger.Subcomponent;

/**
 * Created by DEV on 27.07.2017.
 */
@ActivityScope
@Subcomponent(modules = {LoginActivityModule.class})
public interface LoginActivityComponent {
    void inject(SignActivity signActivity);
    void inject(LoginDetailsFragment loginDetailsFragment);
    void inject(RegistrationConfirmFragment confirmFragment);
    void inject(RegistrationFragment registrationFragment);
}
