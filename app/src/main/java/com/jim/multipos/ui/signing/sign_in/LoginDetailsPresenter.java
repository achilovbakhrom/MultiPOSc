package com.jim.multipos.ui.signing.sign_in;

import com.jim.multipos.core.BaseFragmentPresenter;
import com.jim.multipos.ui.signing.sign_in.view.LoginDetailsFragmentView;

/**
 * Created by DEV on 31.07.2017.
 */

public interface LoginDetailsPresenter extends BaseFragmentPresenter<LoginDetailsFragmentView>{
    void registerFounder();
    void loginFounder();
}
