package com.jim.multipos.utils.rxevents.inventory_events;

import com.jim.multipos.data.db.model.products.Vendor;

import lombok.Data;

/**
 * Created by Sirojiddin on 27.02.2018.
 */
@Data
public class VendorEvent {
    private Vendor vendor;
    private int type;

    public VendorEvent(Vendor vendor, int type) {
        this.vendor = vendor;
        this.type = type;
    }
}
