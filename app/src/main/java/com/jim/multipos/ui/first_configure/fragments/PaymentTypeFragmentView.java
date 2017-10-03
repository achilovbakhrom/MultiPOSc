package com.jim.multipos.ui.first_configure.fragments;

import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.db.model.currency.Currency;
import java.util.List;

/**
 * Created by user on 01.08.17.
 */

public interface PaymentTypeFragmentView {
    void showCurrencies(List<Currency> currencies);
    void showAccount(List<Account> accounts);
    void clearViews();
    void showRecyclerView(List<PaymentType> paymentTypes);
    void paymentTypeAdded();
    void openNextFragment();
    void showPaymentTypeListEmpty(String error);
    void showAccountError(String error);
    void showPaymentTypeNameError(String error);
    void updateRecyclerView();
    void paymentTypeRemoved();
}
