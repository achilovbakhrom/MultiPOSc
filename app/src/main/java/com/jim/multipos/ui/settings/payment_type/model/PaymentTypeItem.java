package com.jim.multipos.ui.settings.payment_type.model;

import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.PaymentType;

public class PaymentTypeItem {
    private PaymentType paymentType;
    private String name;
    private boolean active;
    private Account account;

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
