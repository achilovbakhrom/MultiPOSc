package com.jim.multipos.ui.reports.payments.data;

public class SummaryPayments {
    long id;
    String name;
    double gotToOrder;
    double percentage;
    public void plusGoToOrder(double v){
        gotToOrder += v;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getGotToOrder() {
        return gotToOrder;
    }

    public void setGotToOrder(double gotToOrder) {
        this.gotToOrder = gotToOrder;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}
