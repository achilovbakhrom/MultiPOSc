package com.jim.multipos.ui.consignment.model;

import com.jim.multipos.data.db.model.products.Product;

public class ProductForIncomeItem {
    private Product product;
    private boolean wasSupplied;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Boolean isWasSupplied() {
        return wasSupplied;
    }

    public void setWasSupplied(boolean wasSupplied) {
        this.wasSupplied = wasSupplied;
    }
}
