package com.jim.multipos.ui.reports.product_profit.data;

import lombok.Data;

@Data
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
}
