package com.jim.multipos.data.db.model.inventory;

public class StockCountCost {
    Long stockId;
    double count;
    double cost;

    public StockCountCost(Long stockId, double count, double cost) {
        this.stockId = stockId;
        this.count = count;
        this.cost = cost;
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
