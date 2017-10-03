package com.jim.multipos.ui.registration;

import com.jim.multipos.ui.registration.fragments.RegistrationConfirmFragment;

/**
 * Created by DEV on 01.08.2017.
 */

public interface LoginView {
    void openLoginDetails();
    void openRegistration();
    void openRegistrationConfirm(RegistrationConfirmFragment confirmFragment);
    void popFromBackStack();
}
