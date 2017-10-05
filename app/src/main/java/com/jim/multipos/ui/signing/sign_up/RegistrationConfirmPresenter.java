package com.jim.multipos.ui.signing.sign_up;

import com.jim.multipos.core.BaseFragmentPresenter;
import com.jim.multipos.core.Presenter;

/**
 * Created by DEV on 31.07.2017.
 */

public interface RegistrationConfirmPresenter extends Presenter {
    void confirm();
    void checkAccessToken();
    void back();
}
