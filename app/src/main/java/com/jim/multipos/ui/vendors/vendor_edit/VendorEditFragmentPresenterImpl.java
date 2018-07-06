package com.jim.multipos.ui.vendors.vendor_edit;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Contact;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.vendors.model.ContactItem;
import com.jim.multipos.utils.rxevents.main_order_events.GlobalEventConstants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class VendorEditFragmentPresenterImpl extends BasePresenterImpl<VendorEditFragmentView> implements VendorEditFragmentPresenter {

    private final DatabaseManager databaseManager;
    private List<ContactItem> contactItems;
    private Vendor vendor;
    private int position = -1;

    @Inject
    protected VendorEditFragmentPresenterImpl(VendorEditFragmentView vendorEditFragmentVirw, DatabaseManager databaseManager) {
        super(vendorEditFragmentVirw);
        this.databaseManager = databaseManager;
        contactItems = new ArrayList<>();
    }

    @Override
    public void setContacts(Vendor vendor) {
        contactItems.clear();
        if (vendor == null) {
            view.fillContactsList(contactItems);
            return;
        }
        List<Contact> contacts = databaseManager.getContactsByVendorId(vendor.getId()).blockingGet();
        for (Contact contact : contacts) {
            ContactItem item = new ContactItem();
            item.setContactDetails(contact.getName());
            item.setType(contact.getType());
            item.setVendor(vendor);
            contactItems.add(item);
        }
        view.fillContactsList(contactItems);
    }

    @Override
    public void addContact(int typeSelectedPosition, String contact) {
        ContactItem item = new ContactItem();
        item.setContactDetails(contact);
        item.setType(typeSelectedPosition);
        if (vendor != null) {
            item.setVendor(vendor);
        }
        contactItems.add(contactItems.size(), item);
        view.fillContactsList(contactItems);
    }

    @Override
    public void setVendor(Vendor vendor, int position) {
        this.vendor = vendor;
        this.position = position;
    }

    @Override
    public void saveVendor(String name, String address, String contactName, boolean checked, String photoSelected) {
        if (this.vendor == null) {
            if (databaseManager.isVendorNameExist(name).blockingSingle()) {
                view.setVendorNameError();
                return;
            }
            vendor = new Vendor();
            vendor.setName(name);
            vendor.setContactName(contactName);
            vendor.setAddress(address);
            vendor.setPhotoPath(photoSelected);
            vendor.setActive(checked);
            databaseManager.addVendor(vendor).subscribe(id -> {
                for (ContactItem item : contactItems) {
                    Contact contact = new Contact();
                    contact.setVendorId(id);
                    contact.setName(item.getContactDetails());
                    contact.setType(item.getType());
                    databaseManager.addContact(contact).blockingSingle();
                }
                view.sendEvent(GlobalEventConstants.ADD, vendor);
                view.refreshVendorsList();
            });

        } else {
            if (databaseManager.isVendorNameExist(name).blockingSingle()) {
                if (!vendor.getName().equals(name)) {
                    view.setVendorNameError();
                    return;
                }
            }
            databaseManager.removeAllContacts(vendor.getId()).subscribe();
            vendor.keepToHistory();
            vendor.setName(name);
            vendor.setContactName(contactName);
            vendor.setAddress(address);
            vendor.setActive(checked);
            vendor.setPhotoPath(photoSelected);
            databaseManager.addVendor(vendor).subscribe(id -> {
                for (ContactItem item : contactItems) {
                    Contact contact = new Contact();
                    contact.setVendorId(id);
                    contact.setName(item.getContactDetails());
                    contact.setType(item.getType());
                    databaseManager.addContact(contact).blockingSingle();
                }
                view.sendEvent(GlobalEventConstants.UPDATE, vendor);
                view.notifyItemUpdated();
            });
        }

    }

    @Override
    public void deleteVendor() {
        if (vendor.getActive()){
            view.showCantDeleteWarningDialog();
            return;
        }
        vendor.setDeleted(true);
        databaseManager.addVendor(vendor).subscribe(aLong -> {
            view.refreshVendorsList();
            view.sendEvent(GlobalEventConstants.DELETE, vendor);
        });
    }

    @Override
    public boolean checkChanges(String name, String address, String contactName, boolean checked, String photoPath) {
        if (vendor == null) {
            return !name.equals("") || !address.equals("") || !contactName.equals("") || !checked || !photoPath.equals("") || contactItems.size() > 0;
        } else {
            return !name.equals(vendor.getName()) || !address.equals(vendor.getAddress()) || !contactName.equals(vendor.getContactName()) || checked != vendor.getActive() || !photoPath.equals(vendor.getPhotoPath());
        }
    }
}
