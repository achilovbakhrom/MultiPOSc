package com.jim.multipos.ui.registration.presenters;


import com.jim.multipos.ui.registration.fragments.RegistrationConfirmFragmentView;

/**
 * Created by DEV on 31.07.2017.
 */

public class RegistrationConfirmPresenterImpl implements RegistrationConfirmPresenter {
    private RegistrationConfirmFragmentView view;

    public RegistrationConfirmPresenterImpl() {
            }

    @Override
    public void init(RegistrationConfirmFragmentView view) {
        this.view = view;
    }

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
