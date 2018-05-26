package com.jim.multipos.utils.rxevents.inventory_events;


/**
 * Created by Sirojiddin on 27.02.2018.
 */
public class BillingOperationEvent {
    int type;

    public BillingOperationEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
