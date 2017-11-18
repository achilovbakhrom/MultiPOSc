package com.jim.multipos.data.db.model.intosystem;

/**
 * Created by Sirojiddin on 16.11.2017.
 */
public class ProductCost {
    private Long vendorId;
    private Double cost;
    public Double getCost() {
        return cost;
    }
    public void setCost(Double cost) {
        this.cost = cost;
    }
    public Long getVendorId() {
        return vendorId;
    }
    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }
}
