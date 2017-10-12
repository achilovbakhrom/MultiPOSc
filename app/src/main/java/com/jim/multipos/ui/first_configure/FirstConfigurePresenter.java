package com.jim.multipos.ui.first_configure;

import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.jim.mpviews.MpSpinner;
import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.currency.Currency;

import java.util.Map;

/**
 * Created by user on 07.10.17.
 */

public interface FirstConfigurePresenter extends Presenter {
    void openNextFragment();
    void openNextFragment(int position);
    void openPrevFragment();
    void fillPosDetailsData(EditText etPosId, EditText etAlias, EditText etAddress, EditText etPassword, EditText etRepeatPassword);
    void fillAccountsRV(RecyclerView recyclerView);
    void fillPaymentTypesRV(RecyclerView recyclerView);
    void setupCurrencyData();
    void changeCurrency(Currency currency);
    void addAccount(String name, int type, int circulation);
    void addPaymentType(String name, Account account);
    String[] getCirculations();
    String[] getTypes();
    Boolean isAccountNameExists(String name);
    void checkAccountData();
    void checkPaymentTypeData();
    void savePosDetailsData(String posId, String alias, String address, String password);
    boolean isNextButton();
    void setupPaymentTypeCurrency();
    void setupPaymentTypeAccount();
}
