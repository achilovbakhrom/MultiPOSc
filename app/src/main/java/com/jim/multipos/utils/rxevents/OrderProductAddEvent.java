package com.jim.multipos.utils.rxevents;

import lombok.Data;

/**
 * Created by developer on 28.12.2017.
 */
public class OrderProductAddEvent {
    Long productId;
    String message;

    public OrderProductAddEvent(Long productId, String message) {
        this.productId = productId;
        this.message = message;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
