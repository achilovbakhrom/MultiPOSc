package com.jim.multipos.data.db.model.intosystem;

import com.jim.multipos.data.db.model.customer.Customer;

/**
 * Created by Sirojiddin on 27.12.2017.
 */
public class NotificationData {
    private Customer customer;
    private double totalPayments;
    private double totalDiscounts;
    private String abbr;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public double getTotalPayments() {
        return totalPayments;
    }

    public void setTotalPayments(double totalPayments) {
        this.totalPayments = totalPayments;
    }

    public double getTotalDiscounts() {
        return totalDiscounts;
    }

    public void setTotalDiscounts(double totalDiscounts) {
        this.totalDiscounts = totalDiscounts;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }
}
