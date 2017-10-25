package com.jim.multipos.ui.lock_screen;

import com.jim.multipos.core.Presenter;

/**
 * Created by DEV on 04.08.2017.
 */

public interface LockScreenPresenter extends Presenter {
    void checkPassword(String password);
}
