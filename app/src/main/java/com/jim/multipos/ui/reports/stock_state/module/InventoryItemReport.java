package com.jim.multipos.ui.reports.stock_state.module;

import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;

public class InventoryItemReport {
    Product product;
    double available;
    double sumCost;

    Vendor vendor;

    public InventoryItemReport(){

    }
    public InventoryItemReport(Product product, double available, double sumCost) {
        this.product = product;
        this.available = available;
        this.sumCost = sumCost;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getAvailable() {
        return available;
    }

    public void setAvailable(double available) {
        this.available = available;
    }

    public double getSumCost() {
        return sumCost;
    }

    public void setSumCost(double sumCost) {
        this.sumCost = sumCost;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }
}
