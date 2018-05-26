package com.jim.multipos.ui.reports.inventory.model;

import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;

public class InventorySummary {
    private Product product;
    private Vendor vendor;
    private double sold;
    private double receivedFromVendor;
    private double retrunToVendor;
    private double retrunFromCustomer;
    private double voidIncome;
    private double wasted;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public double getSold() {
        return sold;
    }

    public void setSold(double sold) {
        this.sold = sold;
    }

    public double getReceivedFromVendor() {
        return receivedFromVendor;
    }

    public void setReceivedFromVendor(double receivedFromVendor) {
        this.receivedFromVendor = receivedFromVendor;
    }

    public double getRetrunToVendor() {
        return retrunToVendor;
    }

    public void setRetrunToVendor(double retrunToVendor) {
        this.retrunToVendor = retrunToVendor;
    }

    public double getRetrunFromCustomer() {
        return retrunFromCustomer;
    }

    public void setRetrunFromCustomer(double retrunFromCustomer) {
        this.retrunFromCustomer = retrunFromCustomer;
    }

    public double getVoidIncome() {
        return voidIncome;
    }

    public void setVoidIncome(double voidIncome) {
        this.voidIncome = voidIncome;
    }

    public double getWasted() {
        return wasted;
    }

    public void setWasted(double wasted) {
        this.wasted = wasted;
    }
}
