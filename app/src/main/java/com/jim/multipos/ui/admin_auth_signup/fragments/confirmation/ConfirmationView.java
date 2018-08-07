package com.jim.multipos.ui.admin_auth_signup.fragments.confirmation;

import com.jim.multipos.core.BaseView;

public interface ConfirmationView extends BaseView {
    void onSuccess();

    void onError(String error);
}
