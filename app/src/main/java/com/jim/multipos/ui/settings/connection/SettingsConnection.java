package com.jim.multipos.ui.settings.connection;

import android.content.Context;

import com.jim.multipos.ui.settings.payment_type.PaymentTypeSettingsView;

public class SettingsConnection {

    private Context context;
    PaymentTypeSettingsView paymentTypeSettingsView;

    public SettingsConnection(Context context) {
        this.context = context;
    }

    public void setPaymentTypeSettingsView(PaymentTypeSettingsView paymentTypeSettingsView) {
        this.paymentTypeSettingsView = paymentTypeSettingsView;
    }

    public void updateAccounts(){
        if(paymentTypeSettingsView!=null){
            paymentTypeSettingsView.updateAccounts();
        }
    }
}
