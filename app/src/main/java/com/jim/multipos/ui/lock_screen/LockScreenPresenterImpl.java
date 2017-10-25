package com.jim.multipos.ui.lock_screen;

import com.jim.multipos.core.BasePresenterImpl;

import javax.inject.Inject;

/**
 * Created by DEV on 04.08.2017.
 */

public class LockScreenPresenterImpl extends BasePresenterImpl<LockScreenView> implements LockScreenPresenter {

    private static final int EMPTY = 0;
    private static final int WRONG_PASS = 1;

    @Inject
    public LockScreenPresenterImpl(LockScreenView view) {
        super(view);
    }

    @Override
    public void checkPassword(String password) {
        String pass = "123456";
        if (password.isEmpty())
            view.setError(EMPTY);
        else if (pass.equals(password))
            view.successCheck();
        else view.setError(WRONG_PASS);
    }
}
