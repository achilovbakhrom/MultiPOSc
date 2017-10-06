package com.jim.multipos.ui.signing.sign_up.view;

import com.jim.multipos.core.BaseView;

/**
 * Created by DEV on 26.07.2017.
 */

public interface SignUpView extends BaseView{
    String getOrganizationName();
    String getAddress();
    String getZipCode();
    String getEMail();
    void addContactButtonClicked();
    void registerButtonClicked();
    void setErrorToOrganizationName(String error);
    void setErrorToEmail(String error);
    void openConfirmationFragment();
}
