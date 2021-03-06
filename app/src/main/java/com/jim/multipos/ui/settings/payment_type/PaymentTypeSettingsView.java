package com.jim.multipos.ui.settings.payment_type;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.ui.settings.payment_type.model.PaymentTypeItem;

import java.util.List;

public interface PaymentTypeSettingsView extends BaseView {
    void setSpinner(String[] accounts);
    void setPaymentTypes(List<PaymentTypeItem> paymentTypes, List<Account> accountList);
    void setError();
    void notifyList();
    void updateAccounts();
    void setSuccess();
}
