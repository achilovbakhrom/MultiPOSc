package com.jim.multipos.utils.rxevents;

import com.jim.multipos.data.db.model.customer.Customer;

/**
 * Created by user on 05.09.17.
 */

public class CustomerEvent {
    private Customer customer;
    private String eventType;

    public CustomerEvent(Customer customer, String eventType) {
        this.customer = customer;
        this.eventType = eventType;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
