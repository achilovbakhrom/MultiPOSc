package com.jim.mpviews.model;

/**
 * Created by developer on 23.08.2017.
 */

public class PaymentTypeWithService {
    String paymentType;
    String serviceAbr;

    public PaymentTypeWithService(String paymentType, String serviceAbr) {
        this.paymentType = paymentType;
        this.serviceAbr = serviceAbr;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getServiceAbr() {
        return serviceAbr;
    }

    public void setServiceAbr(String serviceAbr) {
        this.serviceAbr = serviceAbr;
    }
}
