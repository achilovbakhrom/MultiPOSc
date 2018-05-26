package com.jim.multipos.ui.mainpospage.model;

import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.ServiceFee;


/**
 * Created by developer on 27.12.2017.
 */
public class ServiceFeeItem {
    private ServiceFee serviceFee;
    private double ammount;

    public ServiceFee getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(ServiceFee serviceFee) {
        this.serviceFee = serviceFee;
    }

    public double getAmmount() {
        return ammount;
    }

    public void setAmmount(double ammount) {
        this.ammount = ammount;
    }
}
