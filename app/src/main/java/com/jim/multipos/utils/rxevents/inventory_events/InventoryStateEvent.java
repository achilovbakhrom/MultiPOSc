package com.jim.multipos.utils.rxevents.inventory_events;

import lombok.Data;

/**
 * Created by Sirojiddin on 27.02.2018.
 */
@Data
public class InventoryStateEvent {
    int type;

    public InventoryStateEvent(int type) {
        this.type = type;
    }
}
