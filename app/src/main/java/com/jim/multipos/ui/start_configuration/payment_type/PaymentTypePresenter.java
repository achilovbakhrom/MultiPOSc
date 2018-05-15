package com.jim.multipos.ui.start_configuration.payment_type;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.PaymentType;

public interface PaymentTypePresenter extends Presenter {
    void initPaymentTypes();
    void addPaymentType(String name, boolean checked, int position);
    void deletePaymentType(PaymentType paymentType, int position);
    void setAppRunFirstTimeValue(boolean state);
}
