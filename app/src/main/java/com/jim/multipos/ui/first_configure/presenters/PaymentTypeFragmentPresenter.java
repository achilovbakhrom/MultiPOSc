package com.jim.multipos.ui.first_configure.presenters;

import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.ui.first_configure.common.BaseFragmentPresenterFirstConfig;
import com.jim.multipos.ui.first_configure.fragments.PaymentTypeFragmentView;

import java.util.List;

/**
 * Created by user on 05.08.17.
 */

public interface PaymentTypeFragmentPresenter extends BaseFragmentPresenterFirstConfig<PaymentTypeFragmentView> {
    boolean isCompleteData();
    void addData(String name, int accountPosition, int currencyPosition);
    /*void showCurrencies();
    void showAccounts();
    void setAdapterData();*/
    void openNextFragment();
    void updatePaymentTypeCurrency();
    void setData();
    void removeItem(int position);
    void updateAccountData(List<Account> accounts);
    void updateCurrencyData(List<Currency> currencies);
}
