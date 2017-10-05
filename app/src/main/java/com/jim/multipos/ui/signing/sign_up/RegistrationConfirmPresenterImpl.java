package com.jim.multipos.ui.signing.sign_up;


/**
 * Created by DEV on 31.07.2017.
 */

public class RegistrationConfirmPresenterImpl extends RegistrationConfirmPresenterImpl {

    private RegistrationConfirmFragmentView view;

    public RegistrationConfirmPresenterImpl() {}


    @Override
    public void confirm() {
        view.onConfirm();
    }

    @Override
    public void checkAccessToken() {

    }

    @Override
    public void back() {
        view.onBack();
    }
}
