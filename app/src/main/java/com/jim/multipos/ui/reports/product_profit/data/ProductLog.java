package com.jim.multipos.ui.reports.product_profit.data;

public class ProductLog {
    String name;
    long date;
    long orderId;
    double qty;
    double costEach;
    double priceEach;
    String discription;
    public double getTotal(){
        return qty*priceEach;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public double getCostEach() {
        return costEach;
    }

    public void setCostEach(double costEach) {
        this.costEach = costEach;
    }

    public double getPriceEach() {
        return priceEach;
    }

    public void setPriceEach(double priceEach) {
        this.priceEach = priceEach;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }
}
