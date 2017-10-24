package com.jim.multipos.ui.vendor.add_edit;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.Contact;
import com.jim.multipos.data.db.model.Vendor;
import com.jim.multipos.ui.vendor.AddingMode;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Achilov Bakhrom on 10/21/17.
 */

public interface VendorAddEditPresenter extends Presenter {
    void addVendor(String name, String contactName, String address, boolean isActive);
    Boolean isVendorNameExists(String name);
    AddingMode getMode();
    void setMode(AddingMode mode, Long vendorId);
    List<Vendor> getVendors();
    String[] getContactTypes();
    void addContact(int contactType, String contactData);
    void removeVendor();
    List<Contact> getContacts();
    void removeContact(Contact contact);
}
