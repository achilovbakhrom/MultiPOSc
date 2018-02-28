package com.jim.multipos.utils.rxevents.main_order_events;

import com.jim.multipos.data.db.model.customer.Customer;

import lombok.Data;

/**
 * Created by developer on 27.02.2018.
 */
@Data
public class CustomerEvent {
    private Customer customer;
    private Customer newCustomer;
    private int type;

    public CustomerEvent(Customer customer, int type) {
        this.customer = customer;
        this.type = type;
    }
}
