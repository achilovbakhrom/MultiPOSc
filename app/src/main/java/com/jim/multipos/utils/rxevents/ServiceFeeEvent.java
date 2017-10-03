package com.jim.multipos.utils.rxevents;

import com.jim.multipos.data.db.model.ServiceFee;

/**
 * Created by user on 31.08.17.
 */

public class ServiceFeeEvent {
    private ServiceFee  serviceFee;
    private String eventType;

    public ServiceFeeEvent(ServiceFee serviceFee, String eventType) {
        this.serviceFee = serviceFee;
        this.eventType = eventType;
    }

    public ServiceFee getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(ServiceFee serviceFee) {
        this.serviceFee = serviceFee;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
