package com.jim.multipos.data.db.model.inventory;

public class OutcomePerfomance {
    private long outcomeId;
    private double count;
    private long stockId = -1;

    public OutcomePerfomance(long outcomeId, double count, long stockId) {
        this.outcomeId = outcomeId;
        this.count = count;
        this.stockId = stockId;
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
}
