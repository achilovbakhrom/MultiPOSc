package com.jim.multipos.ui.registration.presenters;

import android.os.Bundle;

import com.jim.multipos.data.db.model.Contact;
import com.jim.multipos.ui.registration.fragments.RegistrationConfirmFragment;
import com.jim.multipos.ui.registration.fragments.RegistrationFragmentView;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.operations.ContactOperations;

import java.util.ArrayList;
import java.util.Arrays;

import static com.jim.multipos.utils.BundleConstants.ORG_ADDRESS;
import static com.jim.multipos.utils.BundleConstants.ORG_CODE;
import static com.jim.multipos.utils.BundleConstants.ORG_EMAIL;
import static com.jim.multipos.utils.BundleConstants.ORG_NAME;

/**
 * Created by DEV on 26.07.2017.
 */

public class RegistrationPresenterImpl implements RegistrationPresenter {
    private RegistrationFragmentView view;
    private ArrayList<Contact> list;
    private ContactOperations contactOperations;

    public RegistrationPresenterImpl(DatabaseManager databaseManager) {
        list = new ArrayList<>();
        contactOperations = databaseManager;
    }

    @Override
    public void init(RegistrationFragmentView view) {
        this.view = view;
    }

    @Override
    public void setItems(String[] contacts) {
        ArrayList<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(contacts));
        view.setSpinnerItems(list);
    }

    @Override
    public void displayFragment(String name, String address, String email, String code) {
        Bundle bundle = new Bundle();
        bundle.putString(ORG_NAME, name);
        bundle.putString(ORG_ADDRESS, address);
        bundle.putString(ORG_EMAIL, email);
        bundle.putString(ORG_CODE, code);
        RegistrationConfirmFragment confirmFragment = new RegistrationConfirmFragment();
        confirmFragment.setArguments(bundle);
        view.displayFragment(confirmFragment);
    }

    @Override
    public void popBackStack() {
        view.popFromBackStack();
    }

    @Override
    public void wrapData() {
        view.sendData();
    }

    @Override
    public void setRecyclerViewItems(String s, String toString) {
        Contact contact = new Contact();
        contact.setContactType(s);
        contact.setContactValue(toString);
        list.add(contact);
        view.setRecyclerView(list);
    }
}
