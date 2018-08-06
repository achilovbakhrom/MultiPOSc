package com.jim.multipos.ui.admin_auth_signin;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.network.model.SigninResponse;

public interface AdminAuthSigninActivityView extends BaseView {
    void onSignInSucces(SigninResponse response);
    void onSignInError(String error);
}
