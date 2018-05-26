package com.jim.multipos.ui.reports.product_profit.data;

public class ProfitData {
    long id;
    String name;
    double count;
    double totalCost;
    double totalSale;
    double totalDiscount;
    double totalServiceFee;
    public void plusCount(double count){
        this.count += count;
    }
    public void plusCost(double cost){
        this.totalCost += cost;
    }
    public void plusSale(double sale){
        this.totalSale += sale;
    }
    public void plusDiscount(double discount){
        this.totalDiscount += discount;
    }
    public void plusServiceFee(double totalServiceFee){
        this.totalServiceFee += totalServiceFee;
    }
    public double getProfit(){
        return (1-totalCost/totalSale)*100;
    }
    public double getNET(){
        return totalSale - totalCost;
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

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public double getTotalSale() {
        return totalSale;
    }

    public void setTotalSale(double totalSale) {
        this.totalSale = totalSale;
    }

    public double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public double getTotalServiceFee() {
        return totalServiceFee;
    }

    public void setTotalServiceFee(double totalServiceFee) {
        this.totalServiceFee = totalServiceFee;
    }
}
