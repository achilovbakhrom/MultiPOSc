package com.jim.multipos.utils.rxevents;

import com.jim.multipos.data.db.model.customer.CustomerGroup;

/**
 * Created by user on 07.09.17.
 */

public class CustomerGroupEvent {
    private CustomerGroup customerGroup;
    private String eventType;

    public CustomerGroupEvent(CustomerGroup customerGroup, String eventType) {
        this.customerGroup = customerGroup;
        this.eventType = eventType;
    }

    public CustomerGroup getCustomerGroup() {
        return customerGroup;
    }

    public void setCustomerGroup(CustomerGroup customerGroup) {
        this.customerGroup = customerGroup;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
