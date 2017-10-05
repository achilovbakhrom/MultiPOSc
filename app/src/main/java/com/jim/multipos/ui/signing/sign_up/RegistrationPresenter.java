package com.jim.multipos.ui.signing.sign_up;


import com.jim.multipos.core.BaseFragmentPresenter;
import com.jim.multipos.core.Presenter;

/**
 * Created by DEV on 26.07.2017.
 */

public interface RegistrationPresenter extends Presenter {
    void init();
    void addContact();
    void register();
}
