package com.jim.multipos.ui.vendors.vendor_edit;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.vendors.model.ContactItem;

import java.util.List;

public interface VendorEditFragmentView extends BaseView {
    void setSelectedVendor(Vendor vendor, int position);
    void fillContactsList(List<ContactItem> contactItems);
    void sendEvent(int type, Vendor vendor);
    void setVendorNameError();
    void refreshVendorsList();
    void notifyItemUpdated();
    boolean hasChanges();
    void showCantDeleteWarningDialog();
}
