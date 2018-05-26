package com.jim.multipos.utils.rxevents.till_management_events;


/**
 * Created by Sirojiddin on 28.02.2018.
 */
public class TillEvent {
    int type;

    public TillEvent(int type) {
        this.type = type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
