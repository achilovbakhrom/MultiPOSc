package com.jim.multipos.utils.rxevents.main_order_events;

import com.jim.multipos.data.db.model.customer.Customer;


/**
 * Created by developer on 27.02.2018.
 */
public class CustomerEvent {
    private Customer customer;
    private Customer newCustomer;
    private int type;

    public CustomerEvent(Customer customer, int type) {
        this.customer = customer;
        this.type = type;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Customer getNewCustomer() {
        return newCustomer;
    }

    public void setNewCustomer(Customer newCustomer) {
        this.newCustomer = newCustomer;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
