package com.jim.multipos.data.db.model.waybill;


public class Waybill {
    private long number;
    private String dateTime;
    private boolean hasDeadline;
    private boolean inTransit;
    private String description;
    private double totalAmount;

    public Waybill(long number, String dateTime, boolean hasDeadline, boolean inTransit, String description, double totalAmount) {
        this.number = number;
        this.dateTime = dateTime;
        this.hasDeadline = hasDeadline;
        this.inTransit = inTransit;
        this.description = description;
        this.totalAmount = totalAmount;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isHasDeadline() {
        return hasDeadline;
    }

    public void setHasDeadline(boolean hasDeadline) {
        this.hasDeadline = hasDeadline;
    }

    public boolean isInTransit() {
        return inTransit;
    }

    public void setInTransit(boolean inTransit) {
        this.inTransit = inTransit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
