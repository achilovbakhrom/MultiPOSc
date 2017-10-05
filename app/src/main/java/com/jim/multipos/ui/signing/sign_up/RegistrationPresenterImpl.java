package com.jim.multipos.ui.signing.sign_up;

import android.os.Bundle;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.Contact;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.network.MultiPosApiModule;
import com.jim.multipos.data.operations.ContactOperations;
import com.jim.multipos.ui.signing.SignActivity;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;

import static com.jim.multipos.utils.BundleConstants.ORG_ADDRESS;
import static com.jim.multipos.utils.BundleConstants.ORG_CODE;
import static com.jim.multipos.utils.BundleConstants.ORG_EMAIL;
import static com.jim.multipos.utils.BundleConstants.ORG_NAME;
import static com.jim.multipos.utils.CommonUtils.isEmailValid;

/**
 * Created by DEV on 26.07.2017.
 */

public class RegistrationPresenterImpl extends BasePresenterImpl<RegistrationView> implements RegistrationPresenter{

    private final String CONTACTS_KEY = "CONTACTS_KEY";


    private final DatabaseManager databaseManager;

    private List<Contact> contacts;

    @Inject
    public RegistrationPresenterImpl(RegistrationView view, DatabaseManager databaseManager) {
        super(view);
        this.databaseManager = databaseManager;
        contacts = new ArrayList<>();
    }


    @Override
    public void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);
        if (bundle != null) {
            contacts = Parcels.unwrap(bundle.getParcelable(CONTACTS_KEY));
        }
    }

    @Override
    public void addContact(int type, String data) {
        Contact contact = new Contact();
        //TODO create and add Contact object
        contacts.add(contact);
    }

    @Override
    public void register() {
        String code = view.getZipCode();
        String address = view.getAddress();
        String name = view.getOrganizationName();
        String email = view.getEMail();
        if (name.isEmpty()) {
            view.setErrorToOrganizationName("Please, enter organization name");
            return;
        }
        if (email.isEmpty()) {
            view.setErrorToEmail("Please, enter your e-mail address");
            return;
        }
        if (isEmailValid(email)) {
            view.setErrorToEmail("Email is not valid...");
            return;
        }
        //TODO Create and send Registration object by ApiManager
        view.openConfirmationFragment();
    }

    @Override
    public void onSaveInstanceState(@Nullable Bundle bundle) {
        bundle.putParcelable(CONTACTS_KEY, Parcels.wrap(contacts));
        super.onSaveInstanceState(bundle);
    }
}
