package com.jim.multipos.utils.rxevents.inventory_events;

import com.jim.multipos.data.db.model.products.Vendor;


/**
 * Created by Sirojiddin on 27.02.2018.
 */
public class ConsignmentWithVendorEvent {
    Vendor vendor;
    int type;

    public ConsignmentWithVendorEvent(Vendor vendor, int type) {
        this.vendor = vendor;
        this.type = type;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
