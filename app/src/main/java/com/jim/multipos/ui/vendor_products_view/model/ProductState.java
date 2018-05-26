package com.jim.multipos.ui.vendor_products_view.model;

import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;


/**
 * Created by Sirojiddin on 01.03.2018.
 */
public class ProductState {
    private Product product;
    private Vendor vendor;
    private Double value;

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

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
