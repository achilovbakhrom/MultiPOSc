package com.jim.multipos.ui.reports.tills.model;


public class DetailsOfTill {
    private String account;
    double payIn = 0, payOut = 0, bankDrop = 0, payToVendor = 0,
            incomeDebt = 0, cashTransactions = 0, tips = 0, totalStartingCash = 0,
            totalAmount = 0, tillAmountVariance = 0, closedAmount = 0, toNextAmount = 0;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public double getPayIn() {
        return payIn;
    }

    public void setPayIn(double payIn) {
        this.payIn = payIn;
    }

    public double getPayOut() {
        return payOut;
    }

    public void setPayOut(double payOut) {
        this.payOut = payOut;
    }

    public double getBankDrop() {
        return bankDrop;
    }

    public void setBankDrop(double bankDrop) {
        this.bankDrop = bankDrop;
    }

    public double getPayToVendor() {
        return payToVendor;
    }

    public void setPayToVendor(double payToVendor) {
        this.payToVendor = payToVendor;
    }

    public double getIncomeDebt() {
        return incomeDebt;
    }

    public void setIncomeDebt(double incomeDebt) {
        this.incomeDebt = incomeDebt;
    }

    public double getCashTransactions() {
        return cashTransactions;
    }

    public void setCashTransactions(double cashTransactions) {
        this.cashTransactions = cashTransactions;
    }

    public double getTips() {
        return tips;
    }

    public void setTips(double tips) {
        this.tips = tips;
    }

    public double getTotalStartingCash() {
        return totalStartingCash;
    }

    public void setTotalStartingCash(double totalStartingCash) {
        this.totalStartingCash = totalStartingCash;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getTillAmountVariance() {
        return tillAmountVariance;
    }

    public void setTillAmountVariance(double tillAmountVariance) {
        this.tillAmountVariance = tillAmountVariance;
    }

    public double getClosedAmount() {
        return closedAmount;
    }

    public void setClosedAmount(double closedAmount) {
        this.closedAmount = closedAmount;
    }

    public double getToNextAmount() {
        return toNextAmount;
    }

    public void setToNextAmount(double toNextAmount) {
        this.toNextAmount = toNextAmount;
    }
}
