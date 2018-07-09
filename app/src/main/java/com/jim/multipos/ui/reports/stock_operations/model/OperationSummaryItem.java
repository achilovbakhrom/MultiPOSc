package com.jim.multipos.ui.reports.stock_operations.model;

import com.jim.multipos.data.db.model.products.Product;

public class OperationSummaryItem {
    Product product;
    double sales;
    double reciveFromVendor;
    double returnToVendor;
    double returnFromCustomer;
    double surplus;
    double waste;

    public Product  getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getSales() {
        return sales;
    }

    public void setSales(double sales) {
        this.sales = sales;
    }

    public double getReciveFromVendor() {
        return reciveFromVendor;
    }

    public void setReciveFromVendor(double reciveFromVendor) {
        this.reciveFromVendor = reciveFromVendor;
    }

    public double getReturnToVendor() {
        return returnToVendor;
    }

    public void setReturnToVendor(double returnToVendor) {
        this.returnToVendor = returnToVendor;
    }

    public double getReturnFromCustomer() {
        return returnFromCustomer;
    }

    public void setReturnFromCustomer(double returnFromCustomer) {
        this.returnFromCustomer = returnFromCustomer;
    }

    public double getSurplus() {
        return surplus;
    }

    public void setSurplus(double surplus) {
        this.surplus = surplus;
    }

    public double getWaste() {
        return waste;
    }

    public void setWaste(double waste) {
        this.waste = waste;
    }
}
