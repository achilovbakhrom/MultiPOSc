package com.jim.multipos.utils.rxevents.main_order_events;

import com.jim.multipos.data.db.model.ServiceFee;


/**
 * Created by developer on 27.02.2018.
 */
public class ServiceFeeEvent {
    private ServiceFee serviceFee;
    private ServiceFee newServiceFee;
    private int type;

    public ServiceFeeEvent(ServiceFee serviceFee, int type) {
        this.serviceFee = serviceFee;
        this.type = type;
    }

    public ServiceFee getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(ServiceFee serviceFee) {
        this.serviceFee = serviceFee;
    }

    public ServiceFee getNewServiceFee() {
        return newServiceFee;
    }

    public void setNewServiceFee(ServiceFee newServiceFee) {
        this.newServiceFee = newServiceFee;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
