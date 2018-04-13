package com.jim.multipos.ui.reports.customers.model;

import lombok.Data;

@Data
public class CustomerPaymentLog {
    public static final int FOR_ORDER = 0;
    public static final int FOR_DEBT = 1;
    public static final int CHANGE = 2;
    private Long id;
    private String name = "";
    private Long date;
    private int reason;
    private String paymentType = "";
    private double amount;
}
