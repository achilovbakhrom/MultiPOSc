package com.jim.multipos.utils.rxevents.product_events;

import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.products.Category;


/**
 * Created by Sirojiddin on 27.02.2018.
 */
public class ProductClassEvent {
    private ProductClass productClass;
    private int type;

    public ProductClassEvent(ProductClass productClass, int type) {
        this.productClass = productClass;
        this.type = type;
    }

    public ProductClass getProductClass() {
        return productClass;
    }

    public void setProductClass(ProductClass productClass) {
        this.productClass = productClass;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
