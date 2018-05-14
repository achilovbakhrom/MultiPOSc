package com.jim.multipos.ui.lock_screen.auth;

import com.jim.multipos.core.Presenter;

public interface AuthPresenter extends Presenter {
    void checkRegistrationToken(String token);

}
