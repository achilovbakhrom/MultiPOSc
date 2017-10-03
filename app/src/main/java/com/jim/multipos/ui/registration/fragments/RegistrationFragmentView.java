package com.jim.multipos.ui.registration.fragments;

import com.jim.multipos.data.db.model.Contact;

import java.util.ArrayList;

/**
 * Created by DEV on 26.07.2017.
 */

public interface RegistrationFragmentView {
    void setSpinnerItems(ArrayList<String> items);
    void displayFragment(RegistrationConfirmFragment confirmFragment);
    void popFromBackStack();
    void setRecyclerView(ArrayList<Contact> contacts);
    void sendData();
}
