package com.jim.multipos.ui.admin_auth_signup.fragments.info;

import com.jim.multipos.core.BaseView;

public interface InfoView extends BaseView{
    void onSuccess();
    void onError(String error);
}
