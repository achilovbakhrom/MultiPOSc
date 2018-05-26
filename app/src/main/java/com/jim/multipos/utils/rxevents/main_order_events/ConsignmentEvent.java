package com.jim.multipos.utils.rxevents.main_order_events;


/**
 * Created by developer on 27.02.2018.
 */
public class ConsignmentEvent {
    private int type;

    public ConsignmentEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
