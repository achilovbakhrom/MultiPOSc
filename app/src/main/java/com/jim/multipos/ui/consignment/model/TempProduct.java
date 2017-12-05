package com.jim.multipos.ui.consignment.model;

/**
 * Created by Sirojiddin on 01.12.2017.
 */

public class TempProduct {
    private Long id;
    private double count;
    private double cost;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public double getCost() {
        return cost;
    }
}
