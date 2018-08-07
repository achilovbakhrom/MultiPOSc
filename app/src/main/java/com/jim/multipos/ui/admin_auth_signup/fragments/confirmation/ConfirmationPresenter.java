package com.jim.multipos.ui.admin_auth_signup.fragments.confirmation;

import com.jim.multipos.core.Presenter;

public interface ConfirmationPresenter extends Presenter {
    void onConfirmation(String email, int code);
}
