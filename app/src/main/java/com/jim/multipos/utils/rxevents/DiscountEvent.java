package com.jim.multipos.utils.rxevents;

import com.jim.multipos.data.db.model.Discount;

/**
 * Created by Portable-Acer on 10.11.2017.
 */

public class DiscountEvent {
    private Discount discount;
    private String eventType;

    public DiscountEvent(Discount discount, String eventType) {
        this.discount = discount;
        this.eventType = eventType;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
