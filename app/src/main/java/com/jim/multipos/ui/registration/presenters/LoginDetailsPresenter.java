package com.jim.multipos.ui.registration.presenters;

import com.jim.multipos.core.BaseFragmentPresenter;
import com.jim.multipos.ui.registration.fragments.LoginDetailsFragmentView;

/**
 * Created by DEV on 31.07.2017.
 */

public interface LoginDetailsPresenter extends BaseFragmentPresenter<LoginDetailsFragmentView>{
    void registerFounder();
    void loginFounder();
}
