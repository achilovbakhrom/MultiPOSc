package com.jim.multipos.ui.start_configuration.payment_type;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.utils.CompletionMode;

import java.util.List;

public interface PaymentTypeView extends BaseView {
    void setSpinner(String[] accounts);
    void setPaymentTypes(List<PaymentType> paymentTypes);
    void setError();
    void notifyList();
    void setMode(CompletionMode mode);
    void checkCompletion();
    void updateAccounts();
}
