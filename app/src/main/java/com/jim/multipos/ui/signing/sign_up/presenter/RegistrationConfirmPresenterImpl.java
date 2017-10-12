package com.jim.multipos.ui.signing.sign_up.presenter;


import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.core.Presenter;
import com.jim.multipos.ui.signing.sign_up.view.RegistrationConfirmView;

import javax.inject.Inject;

/**
 * Created by DEV on 31.07.2017.
 */

public class RegistrationConfirmPresenterImpl extends BasePresenterImpl<RegistrationConfirmView> implements RegistrationConfirmPresenter{

    @Inject
    protected RegistrationConfirmPresenterImpl(RegistrationConfirmView registrationConfirmView) {
        super(registrationConfirmView);

    }

}
