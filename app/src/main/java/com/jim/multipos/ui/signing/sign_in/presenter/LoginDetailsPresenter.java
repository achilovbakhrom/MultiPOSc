package com.jim.multipos.ui.signing.sign_in.presenter;

import com.jim.multipos.core.BaseFragmentPresenter;
import com.jim.multipos.ui.signing.sign_in.view.LoginDetailsView;

/**
 * Created by DEV on 31.07.2017.
 */

public interface LoginDetailsPresenter extends BaseFragmentPresenter<LoginDetailsView>{
    void registerFounder();
    void loginFounder();
}
