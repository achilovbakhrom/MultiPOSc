package com.jim.multipos.ui.vendor.add_edit;

import android.os.Bundle;
import android.util.Log;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Contact;
import com.jim.multipos.data.db.model.Vendor;
import com.jim.multipos.ui.vendor.AddingMode;
import com.jim.multipos.ui.vendor.add_edit.fragment.VendorAddEditFragment;
import com.jim.multipos.utils.UIUtils;

import org.apache.commons.collections4.list.PredicatedList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Achilov Bakhrom on 10/21/17.
 */

public class VendorAddEditPresenterImpl extends BasePresenterImpl<VendorAddEditView> implements VendorAddEditPresenter{

    private final VendorAddEditView view;

    @Getter
    private AddingMode mode;

    @Setter
    private Long vendorId = -1L;

    private DatabaseManager databaseManager;

    private final String[] contactTypes;
    @Getter
    private List<Contact> contacts;

    @Inject
    VendorAddEditPresenterImpl(VendorAddEditView vendorAddEditView, DatabaseManager databaseManager,
                               @Named("contact_types") String[] contactTypes) {
        super(vendorAddEditView);
        view = vendorAddEditView;
        this.databaseManager = databaseManager;
        this.contactTypes = contactTypes;
        contacts = new ArrayList<>();
    }

    @Override
    public void addVendor(String name, String contactName, String address, boolean isActive) {
        Vendor vendor;
        switch (mode) {
            case ADD:
                vendor = new Vendor();
                vendor.setName(name);
                vendor.setContactName(contactName);
                vendor.setAddress(address);
                vendor.setActive(isActive);
                databaseManager.addVendor(vendor).subscribe(id -> {
                    for (Contact contact : contacts) {
                        contact.setVendorId(id);
                        databaseManager.addContact(contact).subscribe(contactId -> Log.d("sss", "addVendor: " + contactId));
                    }
                });
                break;
            case EDIT:
                vendor = databaseManager.getVendorById(vendorId).blockingSingle();
                databaseManager.removeAllContacts(vendor.getId()).blockingSingle();
                vendor.setName(name);
                vendor.setContactName(contactName);
                vendor.setAddress(address);
                vendor.setActive(isActive);
                databaseManager.addVendor(vendor).subscribe(id -> {
                    for (Contact contact : contacts) {
                        contact.setVendorId(id);
                        databaseManager.addContact(contact).subscribe(contactId -> Log.d("sss", "addVendor: " + contactId));
                    }
                    databaseManager.updateContacts(id, contacts);
                });
                break;
        }
        view.refreshVendorsList();
    }

    @Override
    public Boolean isVendorNameExists(String name) {
        return databaseManager.isVendorNameExist(name).blockingSingle();
    }

    @Override
    public void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);
        if (bundle != null) {
            vendorId = bundle.getLong(VendorAddEditActivity.VENDOR_ID, -1L);
            mode = (AddingMode) bundle.getSerializable(VendorAddEditActivity.ADDING_MODE_KEY);
        }
    }

    @Override
    public void onSaveInstanceState(@Nullable Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (bundle != null) {
            bundle.putSerializable(VendorAddEditActivity.ADDING_MODE_KEY, mode);
            bundle.putLong(VendorAddEditActivity.VENDOR_ID, vendorId);
        }
    }

    @Override
    public List<Vendor> getVendors() {
        List<Vendor> result = databaseManager.getVendors().blockingSingle();
        Collections.sort(result, (o1, o2) -> o2.getIsActive().compareTo(o1.isActive()));
        return result;
    }

    @Override
    public String[] getContactTypes() {
        return contactTypes;
    }

    @Override
    public void addContact(int contactType, String contactData) {
        Contact contact = new Contact();
        contact.setType(contactType);
        contact.setName(contactData);
        contacts.add(contact);
        view.addContactToAddEditView(contact);
    }

    @Override
    public void setMode(AddingMode mode, Long vendorId) {
        if (mode == AddingMode.ADD) {
            if (view.isChangeDetected()) {
                view.showAddEditChangeMessage(new UIUtils.AlertListener() {
                    @Override
                    public void onPositiveButtonClicked() {
                        VendorAddEditPresenterImpl.this.vendorId = -1L;
                        contacts = new ArrayList<>();
                        view.prepareAddMode();
                        VendorAddEditPresenterImpl.this.mode = mode;
                    }

                    @Override
                    public void onNegativeButtonClicked() {
                        view.discardChanges();
                    }
                });
            } else {
                this.vendorId = -1L;
                contacts = new ArrayList<>();
                view.prepareAddMode();
                view.changeSelectedPosition();
                this.mode = mode;
            }
        } else {
            if (view.isChangeDetected()) {
                view.showAddEditChangeMessage(new UIUtils.AlertListener() {
                      @Override
                      public void onPositiveButtonClicked() {
                          VendorAddEditPresenterImpl.this.vendorId = vendorId;
                          Vendor vendor = databaseManager.getVendorById(vendorId).blockingSingle();
                          if (vendor != null) {
                              contacts = vendor.getContacts();
                              vendor.resetContacts();
                              view.prepareEditMode(vendor);
                              VendorAddEditPresenterImpl.this.mode = mode;
                          }
                      }

                      @Override
                      public void onNegativeButtonClicked() {
                          view.discardChanges();
                      }
                });
            } else  {
                this.vendorId = vendorId;
                Vendor vendor = databaseManager.getVendorById(vendorId).blockingSingle();
                if (vendor != null) {
                    contacts = vendor.getContacts();
                    vendor.resetContacts();
                    view.prepareEditMode(vendor);
                }
                view.changeSelectedPosition();
                this.mode = mode;
            }
        }

    }

    @Override
    public void removeVendor() {
        if (vendorId != -1) {
            databaseManager.deleteVendor(vendorId).subscribe(isDeleted -> {
                if (isDeleted) {
                    view.refreshVendorsList();
                }
            });
        }
    }

    @Override
    public void removeContact(Contact contact) {
        if (contacts != null && !contacts.isEmpty()) {
            contacts.remove(contact);
        }
        view.removeContact(contact);
    }

    @Override
    public Vendor getVendor() {
        if (vendorId == -1L)
            return null;
        else
            return databaseManager.getVendorById(vendorId).blockingSingle();
    }
}
