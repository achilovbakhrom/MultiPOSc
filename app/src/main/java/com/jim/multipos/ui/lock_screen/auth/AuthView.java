package com.jim.multipos.ui.lock_screen.auth;

import com.jim.multipos.core.BaseView;

public interface AuthView extends BaseView {
    void setSerialNumberToView(String serial);
    void invalidToken();
    void valid();
}
