package com.jim.multipos.utils.rxevents.main_order_events;

public class DeviceAttachEvent {
    String productName;
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }

    public DeviceAttachEvent(String productName) {
        this.productName = productName;
    }
}
