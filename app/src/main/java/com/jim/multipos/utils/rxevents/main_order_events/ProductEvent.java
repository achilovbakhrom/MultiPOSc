package com.jim.multipos.utils.rxevents.main_order_events;

import com.jim.multipos.data.db.model.products.Product;


/**
 * Created by user on 05.09.17.
 */
public class ProductEvent {
    private Product product;
    private Product newProduct;
    private int type;
    public ProductEvent(Product product, int type) {
        this.product = product;
        this.type = type;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Product getNewProduct() {
        return newProduct;
    }

    public void setNewProduct(Product newProduct) {
        this.newProduct = newProduct;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
