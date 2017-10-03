package com.jim.multipos.data.db.model.waybill;


public class WaybillItem {
    private double cost;
    private double price;
    private long count;

    public WaybillItem(double cost, double price, long count) {
        this.cost = cost;
        this.price = price;
        this.count = count;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
