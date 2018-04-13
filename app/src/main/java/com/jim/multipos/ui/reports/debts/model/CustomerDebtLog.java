package com.jim.multipos.ui.reports.debts.model;

import lombok.Data;

@Data
public class CustomerDebtLog {
    public static final int DEBT_TAKEN = 0;
    public static final int DEBT_CLOSED = 1;
    private String name;
    private Long date;
    private long orderId;
    private int action;
    private double amount;
    private String paymentTypes = "";
    private String groups = "";
}
