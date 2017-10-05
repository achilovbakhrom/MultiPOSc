package com.jim.multipos.ui.signing.sign_in;

/**
 * Created by DEV on 01.08.2017.
 */

public class LoginPresenterImpl implements LoginPresenter {

    private LoginView view;

    public LoginPresenterImpl(LoginView view) {
        this.view = view;
    }

    @Override
    public void openLoginDetails() {
        view.openLoginDetails();
    }
}
