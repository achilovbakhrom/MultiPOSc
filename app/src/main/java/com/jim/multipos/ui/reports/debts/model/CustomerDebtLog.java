package com.jim.multipos.ui.reports.debts.model;


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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentTypes() {
        return paymentTypes;
    }

    public void setPaymentTypes(String paymentTypes) {
        this.paymentTypes = paymentTypes;
    }

    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }
}
