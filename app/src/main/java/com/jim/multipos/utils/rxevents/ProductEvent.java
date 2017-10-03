package com.jim.multipos.utils.rxevents;

import com.jim.multipos.data.db.model.products.Product;

/**
 * Created by user on 05.09.17.
 */

public class ProductEvent {
    private Product product;
    private String eventType;

    public ProductEvent(Product product, String eventType) {
        this.product = product;
        this.eventType = eventType;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
