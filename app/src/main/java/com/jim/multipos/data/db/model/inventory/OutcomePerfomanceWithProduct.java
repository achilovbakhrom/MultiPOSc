package com.jim.multipos.data.db.model.inventory;

public class OutcomePerfomanceWithProduct {
    private long outcomeId;
    private double count;
    private long stockId = -1;
    private long productId;

    public OutcomePerfomanceWithProduct(long outcomeId, double count, long stockId, long productId) {
        this.outcomeId = outcomeId;
        this.count = count;
        this.stockId = stockId;
        this.productId = productId;
        }

    public long getOutcomeId() {
        return outcomeId;
    }

    public void setOutcomeId(long outcomeId) {
        this.outcomeId = outcomeId;
    }

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }

    public long getStockId() {
        return stockId;
    }

    public void setStockId(long stockId) {
        this.stockId = stockId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }
}
