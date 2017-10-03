package com.jim.multipos.data.db.model.employee;


public class SalaryRate {
    private String fromDate;
    private double salaryAmount;
    private String salaryType;

    public SalaryRate(String fromDate, double salaryAmount, String salaryType) {
        this.fromDate = fromDate;
        this.salaryAmount = salaryAmount;
        this.salaryType = salaryType;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public double getSalaryAmount() {
        return salaryAmount;
    }

    public void setSalaryAmount(double salaryAmount) {
        this.salaryAmount = salaryAmount;
    }

    public String getSalaryType() {
        return salaryType;
    }

    public void setSalaryType(String salaryType) {
        this.salaryType = salaryType;
    }
}
