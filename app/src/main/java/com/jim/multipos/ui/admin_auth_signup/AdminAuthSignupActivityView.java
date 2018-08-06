package com.jim.multipos.ui.admin_auth_signup;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.network.model.SignupResponse;

public interface AdminAuthSignupActivityView extends BaseView {
    void onError(String error);
    void onSuccess(SignupResponse response);
    void onConfirmationSuccess();
    void onConfirmationIncorrectCode();
    void onConfirmationError(String error);
}
