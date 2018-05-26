package com.jim.multipos.ui.reports.payments.data;

public class PaymentsReport {
    long filterId;
    String paymentName;
    String accountName;
    String reason;
    String description = "";
    long orderId;
    long tillId;
    long date;
    double amount;
    public Long getDate(){
        return date;
    }

    public long getFilterId() {
        return filterId;
    }

    public void setFilterId(long filterId) {
        this.filterId = filterId;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getTillId() {
        return tillId;
    }

    public void setTillId(long tillId) {
        this.tillId = tillId;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
