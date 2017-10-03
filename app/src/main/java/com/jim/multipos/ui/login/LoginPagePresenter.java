package com.jim.multipos.ui.login;

import com.jim.multipos.core.BaseFragmentPresenter;

/**
 * Created by DEV on 04.08.2017.
 */

public interface LoginPagePresenter extends BaseFragmentPresenter<LoginPageView> {
    void loginEmp(String password);

    void setItems(String[] emp, String[] role);

    void loginAdmin(String password);

    void setClockInHours();

    void setClockOutHours();

}
