package com.jim.multipos.ui.vendors.vendor_edit;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.products.Vendor;

public interface VendorEditFragmentPresenter extends Presenter {
    void setContacts(Vendor vendor);
    void addContact(int typeSelectedPosition, String contact);
    void setVendor(Vendor vendor, int position);
    void saveVendor(String name, String address, String contactName, boolean checked, String photoPath);
    void deleteVendor();
    boolean checkChanges(String name, String address, String contactName, boolean checked, String photoPath);
    void checkDeletable();
}
