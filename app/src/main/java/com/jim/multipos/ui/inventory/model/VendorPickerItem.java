package com.jim.multipos.ui.inventory.model;

import com.jim.multipos.data.db.model.products.Vendor;

public class VendorPickerItem {
    private Vendor vendor;
    private boolean wasSupplied;

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public boolean isWasSupplied() {
        return wasSupplied;
    }

    public void setWasSupplied(boolean wasSupplied) {
        this.wasSupplied = wasSupplied;
    }
}
