package com.jim.multipos.data.db.model.intosystem;

import com.jim.multipos.data.db.model.inventory.DetialCount;
import com.jim.multipos.data.db.model.inventory.StockQueue;

import java.util.List;

public class StockQueueItem {
    StockQueue stockQueue;
    List<DetialCount> detialCounts;

    public double getAvailable() {
        return available;
    }

    public void setAvailable(double available) {
        this.available = available;
    }

    double available;

    public StockQueue getStockQueue() {
        return stockQueue;
    }

    public void setStockQueue(StockQueue stockQueue) {
        this.stockQueue = stockQueue;
    }

    public List<DetialCount> getDetialCounts() {
        return detialCounts;
    }

    public void setDetialCounts(List<DetialCount> detialCounts) {
        this.detialCounts = detialCounts;
    }

}
