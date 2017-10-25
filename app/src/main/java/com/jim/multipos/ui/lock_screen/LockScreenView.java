package com.jim.multipos.ui.lock_screen;

import com.jim.multipos.core.BaseView;

/**
 * Created by DEV on 04.08.2017.
 */

public interface LockScreenView extends BaseView {
    void successCheck();
    void setError(int EMPTY);
}
