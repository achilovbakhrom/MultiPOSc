package com.jim.multipos.ui.first_configure_last;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.ui.first_configure_last.adapter.FirstConfigureListItem;


import java.util.List;

import io.reactivex.Observable;


/**
 * Created by user on 07.10.17.
 */

public interface FirstConfigurePresenter extends Presenter {

    public static final int POS_DETAILS_POSITION = 0;
    public static final int ACCOUNT_POSITION = 2;
    public static final int CURRENCY_POSITION = 1;
    public static final int PAYMENT_TYPE_POSITION = 3;
    public static final int UNITS_POSITION = 4;

    List<FirstConfigureListItem> getFirstConfigureListItems();
    void savePOSDetails(String posId, String alias, String address, String password);
    String getPOSId();
    String getPOSAlias();
    String getPOSAddress();
    String getPassword();
    void setCompletedForFragment(String fragmentName, boolean completed);
    Account addAccount(String name);
    void removeAccount(Account account);
    Observable<List<Account>> getObservableAccounts();
    List<Currency> getCurrencies();
    void leftMenuItemClicked(int position);

    void openPOSDetails();
    void openAccount();
    void openCurrency();
    void openPaymentType();

    List<Account> getAccounts();

    void checkPOSDetailsCorrection(String posId, String alias, String address, boolean isPasswordEnteredCorrect, String password);
    void checkAccountCorrection();
    void checkCurrencyCorrection();
    void checkPaymentTypeCorrection();

    void createCurrency(String name, String abbr);

    List<Currency> getDbCurrencies();
    String[] getSpinnerCurrencies();
    List<PaymentType> getPaymentTypes();
    String[] getSpinnerAccounts();
    String getCurrencyName();


    PaymentType addPaymentType(String name, int accountPos);
    void deletePaymentType(PaymentType paymentType);

    boolean isAccountNameExists(String name);
    boolean isPaymentTypeNameExists(String name);
}
