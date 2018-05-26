package com.jim.multipos.ui.inventory.model;

import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;

import java.io.Serializable;


/**
 * Created by developer on 10.11.2017.
 */
public class InventoryItem implements Serializable {
    long id;
    Product product;
    Vendor vendor;
    double inventory;
    double lowStockAlert;

    public Double getLowStockAlert() {
        return lowStockAlert;
    }

    public Double getInventory() {
        return inventory;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public void setInventory(double inventory) {
        this.inventory = inventory;
    }

    public void setLowStockAlert(double lowStockAlert) {
        this.lowStockAlert = lowStockAlert;
    }
}
