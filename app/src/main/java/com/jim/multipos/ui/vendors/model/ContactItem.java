package com.jim.multipos.ui.vendors.model;

import com.jim.multipos.data.db.model.products.Vendor;

public class ContactItem {
    private Vendor vendor;
    private int type;
    private String contactDetails;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(String contactDetails) {
        this.contactDetails = contactDetails;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }
}
