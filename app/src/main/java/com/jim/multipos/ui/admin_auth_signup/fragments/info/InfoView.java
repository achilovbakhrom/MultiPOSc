package com.jim.multipos.ui.admin_auth_signup.fragments.info;

import com.jim.multipos.core.BaseView;

public interface InfoView extends BaseView{
    void onSuccess(String mail);
    void onError(String error);
    void onError(int error);
}
