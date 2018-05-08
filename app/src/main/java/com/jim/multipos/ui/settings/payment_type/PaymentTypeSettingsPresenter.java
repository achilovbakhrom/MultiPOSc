package com.jim.multipos.ui.settings.payment_type;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.PaymentType;

public interface PaymentTypeSettingsPresenter extends Presenter {
    void initPaymentTypes();
    void savePaymentType(PaymentType paymentType);
    void addPaymentType(String name, boolean checked, int position);
}
