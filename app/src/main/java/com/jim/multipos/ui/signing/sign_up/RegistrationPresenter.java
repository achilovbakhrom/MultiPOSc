package com.jim.multipos.ui.signing.sign_up;


import android.os.Bundle;

import com.jim.multipos.core.BaseFragmentPresenter;
import com.jim.multipos.core.Presenter;

/**
 * Created by DEV on 26.07.2017.
 */

public interface RegistrationPresenter extends Presenter {
    void addContact(int type, String data);
    void register();
}
