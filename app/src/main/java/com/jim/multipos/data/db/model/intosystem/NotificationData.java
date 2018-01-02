package com.jim.multipos.data.db.model.intosystem;

import com.jim.multipos.data.db.model.customer.Customer;
import lombok.Data;

/**
 * Created by Sirojiddin on 27.12.2017.
 */
@Data
public class NotificationData {
    private Customer customer;
    private double totalPayments;
    private double totalDiscounts;
    private String abbr;
}
