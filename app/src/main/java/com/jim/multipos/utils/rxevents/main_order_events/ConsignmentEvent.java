package com.jim.multipos.utils.rxevents.main_order_events;

import lombok.Data;

/**
 * Created by developer on 27.02.2018.
 */
@Data
public class ConsignmentEvent {
    private int type;

    public ConsignmentEvent(int type) {
        this.type = type;
    }
}