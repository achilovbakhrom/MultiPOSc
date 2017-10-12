package com.jim.multipos.ui.first_configure;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.db.model.currency.Currency;
import java.util.List;

/**
 * Created by user on 07.10.17.
 */

public interface FirstConfigureView extends BaseView {
    void replaceFragment(int position);
    void openPrevFragment();
    void updateLeftSideFragment(int position);
    void closeActivity();
    void addAccountItem(Account account);
    void addPaymentTypeItem(PaymentType paymentType);
    void removeAccountItem(Account account);
    void removePaymentTypeItem(PaymentType paymentType);
    void setCurrencySpinnerData(List<Currency> currencies, int position);
    void showPaymentTypeCurrencyToast();
    void showPaymentTypeAccountToast();
    void showPaymentTypeToast();
    void setPaymentTypeCurrency(Currency currency);
    void setPaymentTypeAccount(List<Account> accounts);
}
