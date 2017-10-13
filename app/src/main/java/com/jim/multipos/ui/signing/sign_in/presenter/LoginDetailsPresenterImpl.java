package com.jim.multipos.ui.signing.sign_in.presenter;

import com.jim.multipos.ui.signing.sign_in.view.LoginDetailsView;

import javax.inject.Inject;

/**
 * Created by DEV on 31.07.2017.
 */

public class LoginDetailsPresenterImpl implements LoginDetailsPresenter {

    private LoginDetailsView view;

    @Inject
    public LoginDetailsPresenterImpl() {
    }

    @Override
    public void init(LoginDetailsView view) {
        this.view = view;
    }

    @Override
    public void registerFounder() {
        view.onRegistration();
    }

    @Override
    public void loginFounder() {
        view.onLogin();
    }
}
