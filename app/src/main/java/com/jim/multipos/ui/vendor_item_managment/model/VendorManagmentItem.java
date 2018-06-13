package com.jim.multipos.ui.vendor_item_managment.model;

import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;

import java.util.List;


/**
 * Created by developer on 20.11.2017.
 */
public class VendorManagmentItem {
    private Vendor vendor;
    private double debt;
    private List<Product> products;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Double getDebt(){
        return debt;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public void setDebt(double debt) {
        this.debt = debt;
    }
}
