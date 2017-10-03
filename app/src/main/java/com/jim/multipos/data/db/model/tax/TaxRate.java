package com.jim.multipos.data.db.model.tax;


public class TaxRate {
    private double rate;
    private String fromDate;
    private String toDate;

    public TaxRate(double rate, String fromDate, String toDate) {
        this.rate = rate;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
}
