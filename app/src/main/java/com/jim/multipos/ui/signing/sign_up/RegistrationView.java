package com.jim.multipos.ui.signing.sign_up;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.Contact;
import com.jim.multipos.ui.signing.adapters.ContactsAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DEV on 26.07.2017.
 */

public interface RegistrationView extends BaseView{
    String getOrganizationName();
    String getAddress();
    String getZipCode();
    String getEMail();
    void registerButtonClicked();
    void setErrorToOrganizationName(String error);
    void setErrorToEmail(String error);
    void openConfirmationFragment();
}
