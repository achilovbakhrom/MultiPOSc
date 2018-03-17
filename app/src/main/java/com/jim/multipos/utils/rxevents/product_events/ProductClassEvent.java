package com.jim.multipos.utils.rxevents.product_events;

import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.products.Category;

import lombok.Data;

/**
 * Created by Sirojiddin on 27.02.2018.
 */
@Data
public class ProductClassEvent {
    private ProductClass productClass;
    private int type;

    public ProductClassEvent(ProductClass productClass, int type) {
        this.productClass = productClass;
        this.type = type;
    }
}
