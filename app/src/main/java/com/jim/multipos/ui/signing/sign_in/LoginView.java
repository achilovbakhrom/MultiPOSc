package com.jim.multipos.ui.signing.sign_in;

import com.jim.multipos.ui.signing.sign_up.view.RegistrationConfirmFragment;

/**
 * Created by DEV on 01.08.2017.
 */

public interface LoginView {
    void openLoginDetails();
    void openRegistration();
    void openRegistrationConfirm(RegistrationConfirmFragment confirmFragment);
    void popFromBackStack();
}
