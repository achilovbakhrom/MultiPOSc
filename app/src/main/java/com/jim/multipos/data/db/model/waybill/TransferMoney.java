package com.jim.multipos.data.db.model.waybill;


public class TransferMoney {
    private String dateTime;
    private double paymentAmount;
    private String description;

    public TransferMoney(String dateTime, double paymentAmount, String description) {
        this.dateTime = dateTime;
        this.paymentAmount = paymentAmount;
        this.description = description;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
