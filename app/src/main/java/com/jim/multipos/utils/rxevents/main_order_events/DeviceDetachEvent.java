package com.jim.multipos.utils.rxevents.main_order_events;

public class DeviceDetachEvent {
    String productName;
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }

    public DeviceDetachEvent(String productName) {
        this.productName = productName;
    }
}
