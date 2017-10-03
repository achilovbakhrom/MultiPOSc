package com.jim.multipos.ui.registration.presenters;

import com.jim.multipos.core.BaseFragmentPresenter;
import com.jim.multipos.ui.registration.fragments.RegistrationConfirmFragmentView;

/**
 * Created by DEV on 31.07.2017.
 */

public interface RegistrationConfirmPresenter extends BaseFragmentPresenter<RegistrationConfirmFragmentView> {
    void confirm();
    void checkAccessToken();
    void back();
}
