package com.jim.multipos.utils.rxevents;

import com.jim.multipos.data.db.model.ProductClass;

/**
 * Created by developer on 30.08.2017.
 */

public class ProductClassEvent {
    ProductClass productClass;
    String eventType;

    public ProductClassEvent(ProductClass productClass, String eventType) {
        this.productClass = productClass;
        this.eventType = eventType;
    }

    public ProductClass getProductClass() {
        return productClass;
    }

    public void setProductClass(ProductClass productClass) {
        this.productClass = productClass;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
