package com.jim.multipos.ui.settings.payment_type;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.ui.settings.payment_type.model.PaymentTypeItem;

public interface PaymentTypeSettingsPresenter extends Presenter {
    void initPaymentTypes();
    void savePaymentType(PaymentTypeItem paymentType);
    void addPaymentType(String name, boolean checked, int position);
}
