package com.jim.multipos.ui.signing.sign_up.presenter;


import com.jim.multipos.core.Presenter;

/**
 * Created by DEV on 26.07.2017.
 */

public interface SignUpPresenter extends Presenter {
    void addContact(int type, String data);
    void deleteContact(int pos);
    void register();
}
