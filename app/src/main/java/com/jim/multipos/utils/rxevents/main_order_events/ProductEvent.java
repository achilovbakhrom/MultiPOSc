package com.jim.multipos.utils.rxevents.main_order_events;

import com.jim.multipos.data.db.model.products.Product;

import lombok.Data;

/**
 * Created by user on 05.09.17.
 */
@Data
public class ProductEvent {
    private Product product;
    private int type;
    public ProductEvent(Product product, int type) {
        this.product = product;
        this.type = type;
    }
}
