package com.jim.multipos.ui.first_configure;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.jim.mpviews.MpSpinner;
import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.prefs.PreferencesHelper;
import com.jim.multipos.ui.first_configure.adapters.CurrencySpinnerAdapter;
import com.jim.multipos.ui.first_configure.adapters.SystemAccountsAdapter;
import com.jim.multipos.ui.first_configure.adapters.SystemPaymentTypesAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.functions.Consumer;
import lombok.Getter;

import static com.jim.multipos.ui.first_configure.Constants.ACCOUNT_FRAGMENT_ID;
import static com.jim.multipos.ui.first_configure.Constants.PAYMENT_TYPE_FRAGMENT_ID;
import static com.jim.multipos.ui.first_configure.Constants.POS_DETAIL_FRAGMENT_ID;

/**
 * Created by user on 07.10.17.
 */

public class FirstConfigurePresenterImpl extends BasePresenterImpl<FirstConfigureView> implements FirstConfigurePresenter {
    @Inject
    PreferencesHelper preferences;
    private FirstConfigureView view;
    @Getter
    private boolean[] completedFragments = null;
    private final DatabaseManager databaseManager;
    @Getter
    private final String[] types;
    @Getter
    private final String[] circulations;
    private final String[] currencyName;
    private final String[] currencyAbbr;

    public static final String COMPLETED_FRAGMENTS_KEY = "COMPLETED_FRAGMENTS_KEY";

    @Inject
    public FirstConfigurePresenterImpl(FirstConfigureView view,
                                       DatabaseManager databaseManager,
                                       @Named(value = "account_circulations") String[] circulations,
                                       @Named(value = "account_types") String[] types,
                                       @Named(value = "currency_name") String[] currencyName,
                                       @Named(value = "currency_abbr") String[] currencyAbbr) {
        super(view);
        this.databaseManager = databaseManager;
        this.view = view;
        completedFragments = new boolean[5];
        this.types = types;
        this.circulations = circulations;
        this.currencyName = currencyName;
        this.currencyAbbr = currencyAbbr;
    }

    @Override
    public void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);
        if (bundle != null) {
            completedFragments = bundle.getBooleanArray(COMPLETED_FRAGMENTS_KEY);
        }
    }

    @Override
    public void onSaveInstanceState(@Nullable Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBooleanArray(COMPLETED_FRAGMENTS_KEY, completedFragments);
    }

    @Override
    public void openNextFragment() {
        if (completedFragments == null) return;

        int nextPosition = -1;
        int notCompletedCount = 0;

        for (int i = 0; i < completedFragments.length; i++) {
            if (!completedFragments[i]) {
                nextPosition = i;
                break;
            }
        }

        for (int i = 0; i < completedFragments.length; i++) {
            if (!completedFragments[i]) {
                notCompletedCount++;
            }
        }

        if (notCompletedCount == 0) {
            view.closeActivity();

            return;
        }

        if (nextPosition != -1) {
            openNextFragment(nextPosition);
        }
    }

    @Override
    public void openNextFragment(int position) {
        view.replaceFragment(position);
        view.updateLeftSideFragment(position);
    }

    @Override
    public void openPrevFragment() {
        view.openPrevFragment();
    }

    @Override
    public boolean isNextButton() {
        if (completedFragments == null) return false;
        int i = 0;
        for (boolean isCompleted : completedFragments) {
            if (!isCompleted) i++;
            if (i > 1) return true;
        }
        return false;
    }

    @Override
    public void fillPosDetailsData(EditText etPosId, EditText etAlias, EditText etAddress, EditText etPassword, EditText etRepeatPassword) {
        etPosId.setText(preferences.getPosDetailPosId());
        etAlias.setText(preferences.getPosDetailAlias());
        etAddress.setText(preferences.getPosDetailAddress());
        etPassword.setText(preferences.getPosDetailPassword());
        etRepeatPassword.setText(preferences.getPosDetailPassword());
    }

    @Override
    public void fillAccountsRV(RecyclerView recyclerView) {
        databaseManager.getAccountOperations().getAllAccounts().subscribe(accounts -> {
            recyclerView.setAdapter(new SystemAccountsAdapter(accounts, types, circulations, account -> {
                databaseManager.getAccountOperations().removeAccount(account).subscribe(aBoolean -> {
                    view.removeAccountItem(account);
                });
            }));
        });
    }

    @Override
    public void fillPaymentTypesRV(RecyclerView recyclerView) {
        databaseManager.getPaymentTypeOperations().getAllPaymentTypes().subscribe(paymentTypes -> {
            recyclerView.setAdapter(new SystemPaymentTypesAdapter(paymentTypes, paymentType -> {
                databaseManager.getPaymentTypeOperations().removePaymentType(paymentType).subscribe(aBoolean -> {
                    view.removePaymentTypeItem(paymentType);
                });
            }));
        });
    }

    @Override
    public void setupCurrencyData() {
        List<Currency> currencies = new ArrayList<>();

        for (int i = 0; i < currencyName.length; i++) {
            Currency currency = new Currency();
            currency.setName(currencyName[i]);
            currency.setAbbr(currencyAbbr[i]);

            currencies.add(currency);
        }

        databaseManager.getCurrencyOperations().getAllCurrencies().subscribe(dbCurrencies -> {
            int position = 0;


            if (!dbCurrencies.isEmpty()) {
                for (int i = 0; i < currencies.size(); i++) {
                    if (currencies.get(i).getAbbr().equals(dbCurrencies.get(0).getAbbr())) {
                        currencies.get(i).setId(dbCurrencies.get(0).getId());
                        position = i;
                    }
                }
            } else {
                databaseManager.getCurrencyOperations().addCurrency(currencies.get(position)).subscribe();
            }

            view.setCurrencySpinnerData(currencies, position);
        });
    }

    @Override
    public void changeCurrency(Currency currency) {
        databaseManager.getCurrencyOperations().removeAllCurrencies().subscribe(aBoolean -> {
            databaseManager.getCurrencyOperations().addCurrency(currency).subscribe();
        });
    }

    @Override
    public void addAccount(String name, int type, int circulation) {
        Account account = new Account();

        account.setName(name);
        account.setType(type);
        account.setCirculation(circulation);

        databaseManager.getAccountOperations().addAccount(account).subscribe(account1 -> {
            view.addAccountItem(account1);
        });
    }

    @Override
    public void addPaymentType(String name, Account account) {
        databaseManager.getCurrencyOperations().getAllCurrencies().subscribe(currencies -> {
            PaymentType paymentType = new PaymentType();
            paymentType.setName(name);
            paymentType.setAccount(account);
            paymentType.setCurrency(currencies.get(0));

            databaseManager.getPaymentTypeOperations().addPaymentType(paymentType).subscribe(aLong -> {
                view.addPaymentTypeItem(paymentType);
            });
        });

    }

    @Override
    public Boolean isAccountNameExists(String name) {
        return databaseManager.getAccountOperations().isAccountNameExists(name);
    }

    @Override
    public void checkAccountData() {
        databaseManager.getAccountOperations().getAllAccounts().subscribe(accounts -> {
            if (accounts.size() > 0) {
                completedFragments[ACCOUNT_FRAGMENT_ID] = true;
                openNextFragment();
            }
        });
    }

    @Override
    public void checkPaymentTypeData() {
        databaseManager.getPaymentTypeOperations().getAllPaymentTypes().subscribe(paymentTypes -> {
            if (paymentTypes.isEmpty()) {
                view.showPaymentTypeToast();
            } else {
                completedFragments[PAYMENT_TYPE_FRAGMENT_ID] = true;
                openNextFragment();
            }
        });
    }

    @Override
    public void savePosDetailsData(String posId, String alias, String address, String password) {
        completedFragments[POS_DETAIL_FRAGMENT_ID] = true;

        preferences.setPosDetailPosId(posId);
        preferences.setPosDetailAlias(alias);
        preferences.setPosDetailAddress(address);
        preferences.setPosDetailPassword(password);
    }

    @Override
    public void setupPaymentTypeCurrency() {
        databaseManager.getCurrencyOperations().getAllCurrencies().subscribe(currencies -> {
            if (currencies.isEmpty()) {
                view.showPaymentTypeCurrencyToast();
            } else {
                view.setPaymentTypeCurrency(currencies.get(0));
            }
        });
    }

    @Override
    public void setupPaymentTypeAccount() {
        databaseManager.getAccountOperations().getAllAccounts().subscribe(accounts -> {
            if (accounts.isEmpty()) {
                view.showPaymentTypeAccountToast();
            } else {
                view.setPaymentTypeAccount(accounts);
            }
        });
    }
}
