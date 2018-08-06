package com.jim.multipos.ui.admin_auth_signin;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.network.model.Signin;
import com.jim.multipos.data.network.model.SigninResponse;

public interface AdminAuthSigninActivityPresenter extends Presenter {
    void onSignUp(Signin signin);
}
