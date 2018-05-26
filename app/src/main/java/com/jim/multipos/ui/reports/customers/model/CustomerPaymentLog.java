package com.jim.multipos.ui.reports.customers.model;


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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
