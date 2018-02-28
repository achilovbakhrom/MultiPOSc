package com.jim.multipos.utils.rxevents.till_management_events;

import lombok.Data;

/**
 * Created by Sirojiddin on 28.02.2018.
 */
@Data
public class TillEvent {
    int type;

    public TillEvent(int type) {
        this.type = type;
    }
}
