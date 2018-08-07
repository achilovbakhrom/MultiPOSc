package com.jim.multipos.ui.admin_auth_signup.fragments.general;

import com.jim.multipos.core.Presenter;

public interface GeneralPresenter extends Presenter {
    void onVerifyData(String email, String password, String confirmPassword);
}
