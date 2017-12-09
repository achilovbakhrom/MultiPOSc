package com.jim.multipos.ui.vendor.add_edit;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.Contact;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.utils.UIUtils;

/**
 * Created by bakhrom on 10/21/17.
 */

public interface VendorAddEditView extends BaseView{
    void refreshVendorsList();
    void prepareAddMode();
    void prepareEditMode(Vendor vendor);
    void selectAddItem();
    void selectItem(int position);
    void addContactToAddEditView(Contact contact);
    void removeContact(Contact contact);
    void showCantDeleteActiveItemMessage();
    void showAddEditChangeMessage(UIUtils.AlertListener listener);
    boolean isChangeDetected();
    void discardChanges();
    void changeSelectedPosition();
    void sendEvent(String state, Long vendorId);
}
