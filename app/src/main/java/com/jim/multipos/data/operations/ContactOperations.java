package com.jim.multipos.data.operations;

import com.jim.multipos.data.db.model.Contact;
import com.jim.multipos.data.db.model.employee.Employee;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;


/**
 * Created by developer on 08.08.2017.
 */

public interface ContactOperations {
    ArrayList<Contact> getContactsOrganization();
    Observable<Long> addContact(Contact contact);
    Observable<Boolean> addContact(List<Contact> contacts );
    Observable<List<Contact>> getAllContacts();
}
