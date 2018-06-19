package com.jim.multipos.data.db.model.inventory;

public class StockPerfomance {
    private long stockId;
    private double available;
    private double cost;

    public StockPerfomance(long stockId, double available, double cost) {
        this.stockId = stockId;
        this.available = available;
        this.cost = cost;
    }

    public long getStockId() {
        return stockId;
    }

    public void setStockId(long stockId) {
        this.stockId = stockId;
    }

    public double getAvailable() {
        return available;
    }

    public void setAvailable(double available) {
        this.available = available;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
