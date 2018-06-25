package com.jim.multipos.data.db.model.inventory;

public class StockPerfomanceWithProduct {
    private long stockId;
    private double available;
    private double cost;
    private long productId;
    private long expiredDate;

    public StockPerfomanceWithProduct(long stockId, double available, double cost, long productId, long expiredDate) {
        this.stockId = stockId;
        this.available = available;
        this.cost = cost;
        this.productId = productId;
        this.expiredDate = expiredDate;
    }

    public Long getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(long expiredDate) {
        this.expiredDate = expiredDate;
    }

    public Long getStockId() {
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

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }
}
