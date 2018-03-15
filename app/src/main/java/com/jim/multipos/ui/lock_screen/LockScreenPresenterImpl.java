package com.jim.multipos.ui.lock_screen;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.prefs.PreferencesHelper;

import javax.inject.Inject;

/**
 * Created by DEV on 04.08.2017.
 */

public class LockScreenPresenterImpl extends BasePresenterImpl<LockScreenView> implements LockScreenPresenter {

    private static final int EMPTY = 0;
    private static final int WRONG_PASS = 1;
    private PreferencesHelper preferencesHelper;

    @Inject
    public LockScreenPresenterImpl(LockScreenView view, PreferencesHelper preferencesHelper) {
        super(view);
        this.preferencesHelper = preferencesHelper;
    }

    @Override
    public void checkPassword(String password) {
        String pass = preferencesHelper.getPosDetailPassword();
        if (password.isEmpty())
            view.setError(EMPTY);
        else if (pass.equals(password))
            view.successCheck();
        else view.setError(WRONG_PASS);
    }
}
