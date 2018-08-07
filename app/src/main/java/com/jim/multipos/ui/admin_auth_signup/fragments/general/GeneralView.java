package com.jim.multipos.ui.admin_auth_signup.fragments.general;

import com.jim.multipos.core.BaseView;

public interface GeneralView extends BaseView{
    void onSuccess(String mail, String password);
    void onError(int error, int type);
}
