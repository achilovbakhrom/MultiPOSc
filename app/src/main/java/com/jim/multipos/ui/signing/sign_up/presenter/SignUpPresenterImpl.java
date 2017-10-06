package com.jim.multipos.ui.signing.sign_up.presenter;

import android.os.Bundle;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.db.model.Contact;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.ui.signing.sign_up.view.SignUpView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;

import static com.jim.multipos.utils.CommonUtils.isEmailValid;

/**
 * Created by DEV on 26.07.2017.
 */

@PerFragment
public class SignUpPresenterImpl extends BasePresenterImpl<SignUpView> implements SignUpPresenter {

    private final String CONTACTS_KEY = "CONTACTS_KEY";


    private final DatabaseManager databaseManager;

    private List<Contact> contacts;

    @Inject
    public SignUpPresenterImpl(SignUpView view, DatabaseManager databaseManager) {
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
        contact.setType(type);
        contact.setName(data);
        contacts.add(contact);
    }

    @Override
    public void deleteContact(int pos) {
        contacts.remove(pos);
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
