package com.jim.multipos.data.db.model.employee;


public class PayedSalary {
    private String Date;
    private double amount;

    public PayedSalary(String date, double amount) {
        Date = date;
        this.amount = amount;
    }

    public String getDate(){
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
