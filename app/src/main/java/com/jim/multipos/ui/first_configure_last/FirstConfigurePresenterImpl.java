package com.jim.multipos.ui.first_configure_last;

import android.os.Bundle;
import android.util.Log;

import com.jim.mpviews.MpCompletedStateView;
import com.jim.multipos.R;
import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.prefs.PreferencesHelper;
import com.jim.multipos.ui.first_configure_last.adapter.FirstConfigureListItem;
import com.jim.multipos.ui.first_configure_last.fragment.AccountFragment;
import com.jim.multipos.ui.first_configure_last.fragment.CurrencyFragment;
import com.jim.multipos.ui.first_configure_last.fragment.POSDetailsFragment;
import com.jim.multipos.ui.first_configure_last.fragment.PaymentTypeFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import lombok.Getter;

/**
 * Created by user on 07.10.17.
 */

public class FirstConfigurePresenterImpl extends BasePresenterImpl<FirstConfigureView> implements FirstConfigurePresenter {

    static final String POS_DETAILS_KEY = POSDetailsFragment.class.getName();
    static final String ACCOUNT_KEY = AccountFragment.class.getName();
    static final String CURRENCY_KEY = CurrencyFragment.class.getName();
    static final String PAYMENT_TYPE_KEY = PaymentTypeFragment.class.getName();

    PreferencesHelper preferences;
    private FirstConfigureView view;
    @Getter
    private int[] completedFragments = null;
    private final DatabaseManager databaseManager;

    private final String[] currencyName;
    private final String[] currencyAbbr;
    private final String[] firstConfigureListItems;
    private final String[] firstConfigurationItemsDescription;
    public static final String COMPLETED_FRAGMENTS_KEY = "COMPLETED_FRAGMENTS_KEY";
    private Map<String, Boolean> completion = new HashMap<>();

    @Inject
    public FirstConfigurePresenterImpl(FirstConfigureView view,
                                       DatabaseManager databaseManager,
                                       PreferencesHelper preferencesHelper,
                                       @Named(value = "currency_name") String[] currencyName,
                                       @Named(value = "currency_abbr") String[] currencyAbbr,
                                       @Named(value = "first_configure_items") String[] firstConfigureListItems,
                                       @Named(value = "first_configure_items_description") String[] firstConfigurationItemsDescription) {
        super(view);
        this.preferences = preferencesHelper;
        this.databaseManager = databaseManager;
        this.view = view;
        this.completedFragments = new int[5];
        this.currencyName = currencyName;
        this.currencyAbbr = currencyAbbr;
        this.firstConfigureListItems = firstConfigureListItems;
        this.firstConfigurationItemsDescription = firstConfigurationItemsDescription;
    }

    @Override
    public void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);
        if (bundle != null) {
            completion = new HashMap<>();
            completion.put(POS_DETAILS_KEY, bundle.getBoolean(POS_DETAILS_KEY, false));
            completion.put(ACCOUNT_KEY, bundle.getBoolean(ACCOUNT_KEY, false));
            completion.put(CURRENCY_KEY, bundle.getBoolean(CURRENCY_KEY, false));
            completion.put(PAYMENT_TYPE_KEY, bundle.getBoolean(PAYMENT_TYPE_KEY, false));
        } else {
            completion.put(POS_DETAILS_KEY, false);
            completion.put(ACCOUNT_KEY, false);
            completion.put(CURRENCY_KEY, false);
            completion.put(PAYMENT_TYPE_KEY, false);
        }

        if (databaseManager.getAccounts().isEmpty()){
            Account account1 = new Account();
            account1.setName("Cash");
            account1.setStaticAccountType(Account.CASH_ACCOUNT);
            databaseManager.addAccount(account1).blockingSingle();
            Account account = new Account();
            account.setName("DebtAccount");
            account.setStaticAccountType(Account.DEBT_ACCOUNT);
            account.setIsVisible(false);
            databaseManager.addAccount(account).blockingSingle();
            PaymentType paymentType = new PaymentType();
            paymentType.setAccount(account);
            paymentType.setName("ToDebt");
            paymentType.setCurrency(databaseManager.getMainCurrency());
            paymentType.setIsVisible(false);
            databaseManager.addPaymentType(paymentType).blockingSingle();
        }
    }

    @Override
    public void onSaveInstanceState(@Nullable Bundle bundle) {
        super.onSaveInstanceState(bundle);
        for (String key : completion.keySet()) {
            bundle.putBoolean(key, completion.get(key));
        }
    }

    @Override
    public List<FirstConfigureListItem> getFirstConfigureListItems() {
        List<FirstConfigureListItem> result = new ArrayList<>();
        for (int i = 0; i < firstConfigureListItems.length; i++) {
            FirstConfigureListItem item = new FirstConfigureListItem();
            item.setName(firstConfigureListItems[i]);
            item.setDescription(firstConfigurationItemsDescription[i]);
            item.setSelected(i == 0);
            item.setState(MpCompletedStateView.EMPTY_STATE);
            result.add(item);
        }
        return result;
    }

    @Override
    public void savePOSDetails(String posId, String alias, String address, String password) {
        preferences.setPosDetailPosId(posId);
        preferences.setPosDetailAlias(alias);
        preferences.setPosDetailAddress(address);
        preferences.setPosDetailPassword(password);
    }

    @Override
    public String getPOSId() {
        return preferences.getPosDetailPosId();
    }

    @Override
    public String getPOSAlias() {
        return preferences.getPosDetailAlias();
    }

    @Override
    public String getPOSAddress() {
        return preferences.getPosDetailAddress();
    }

    @Override
    public String getPassword() {
        return preferences.getPosDetailPassword();
    }

    @Override
    public void setCompletedForFragment(String fragmentName, boolean completed) {
        completion.put(fragmentName, completed);
    }

    @Override
    public Account addAccount(String name) {
        Account account = new Account();
        account.setName(name);
        return databaseManager.getAccountOperations().addAccount(account).blockingSingle();
    }

    @Override
    public void leftMenuItemClicked(int position) {
        switch (position) {
            case 0:
                openPOSDetails();
                break;
            case 1:
                openAccount();
                break;
            case 2:
                openCurrency();
                break;
            case 3:
                checkAccountCorrection();
                checkCurrencyCorrection();
                openPaymentType();
                break;
        }
    }

    @Override
    public Observable<List<Account>> getObservableAccounts() {
        return databaseManager.getAllAccounts();
    }

    @Override
    public String[] getSpinnerCurrencies() {
        List<Currency> currencyList = getCurrencies();
        String[] items = new String[currencyList.size()];
        for (Currency currency : currencyList) {
            items[currencyList.indexOf(currency)] = currency.getName() + "(" + currency.getAbbr() + ")";
        }
        return items;
    }

    @Override
    public List<Currency> getCurrencies() {
        List<Currency> currencies = new ArrayList<>();
        for (int i = 0; i < currencyName.length; i++) {
            Currency currency = new Currency();
            currency.setIsMain(i == 0);
            currency.setName(currencyName[i]);
            currency.setAbbr(currencyAbbr[i]);
            currency.setActive(true);
            currencies.add(currency);
        }
        return currencies;
    }

    @Override
    public void removeAccount(Account account) {
        databaseManager.removeAccount(account).subscribe(success -> {
            if (success) {
                checkAccountCorrection();
                openAccount();
            } else {
                Log.d("sss", "removeAccount: unsuccessful");
            }
        });
    }

    @Override
    public List<Currency> getDbCurrencies() {
        return databaseManager.getAllCurrencies().blockingSingle();
    }

    @Override
    public void openPOSDetails() {
        CompletionMode mode = CompletionMode.NEXT;
        if (isLast(POS_DETAILS_KEY))
            mode = CompletionMode.FINISH;
        view.openPOSDetailsFragment(mode);
    }

    @Override
    public void openAccount() {
        CompletionMode mode = CompletionMode.NEXT;
        if (isLast(ACCOUNT_KEY))
            mode = CompletionMode.FINISH;
        view.openAccountFragment(mode);
    }

    @Override
    public void openCurrency() {
        CompletionMode mode = CompletionMode.NEXT;
        if (isLast(CURRENCY_KEY))
            mode = CompletionMode.FINISH;
        view.openCurrencyFragment(mode);
    }

    @Override
    public void openPaymentType() {
        if (getAccounts().isEmpty()) {
            view.makeToast(R.string.warning_account_add);
            openAccount();
            return;
        }

        if (getDbCurrencies().isEmpty()) {
            view.makeToast(R.string.warning_currency_selection);
            openCurrency();
            return;
        }

        CompletionMode mode = CompletionMode.NEXT;
        if (isLast(PAYMENT_TYPE_KEY))
            mode = CompletionMode.FINISH;
        view.openPaymentTypeFragment(mode);
    }

    @Override
    public PaymentType addPaymentType(String name, int accountPos) {
        List<Account> accounts = getAccounts();
        List<Currency> currencies = getDbCurrencies();
        PaymentType result = null;
        if (accounts != null && !accounts.isEmpty() && currencies != null && !currencies.isEmpty()) {
            PaymentType paymentType = new PaymentType();
            paymentType.setName(name);
            paymentType.setAccount(accounts.get(accountPos));
            paymentType.setCurrency(currencies.get(0));
            Long id = databaseManager.addPaymentType(paymentType).blockingSingle();
            List<PaymentType> paymentTypes = databaseManager.getAllPaymentTypes().blockingSingle();
            for (PaymentType pt : paymentTypes) {
                if (Objects.equals(pt.getId(), id)) {
                    result = pt;
                    break;
                }
            }
        }
        view.changeState(PAYMENT_TYPE_POSITION, MpCompletedStateView.COMPLETED_STATE);
        completion.put(PAYMENT_TYPE_KEY, true);
        return result;
    }

    private boolean isLast(String name) {
        if (completion != null) {
            int notCompletedCount = 0;
            for (String key : completion.keySet()) {
                if (notCompletedCount > 1) {
                    return false;
                }
                if (!completion.get(key))
                    notCompletedCount++;
            }
            if (notCompletedCount == 1 && !completion.get(name)) {
                return true;
            }
            if (notCompletedCount == 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Account> getAccounts() {
        return databaseManager.getAllAccounts().blockingSingle();
    }

    @Override
    public String[] getSpinnerAccounts() {
        List<Account> accounts = getAccounts();
        String[] items = new String[accounts.size()];
        for (int i = 0; i < items.length; i++) {
            items[i] = accounts.get(i).getName();
        }
        return items;
    }

    @Override
    public void checkPOSDetailsCorrection(String posId, String alias, String address, boolean isPasswordEnteredCorrect, String password) {
        if (posId.isEmpty() || alias.isEmpty() || address.isEmpty() || !isPasswordEnteredCorrect) {
            completion.put(POS_DETAILS_KEY, false);
            view.changeState(POS_DETAILS_POSITION, MpCompletedStateView.WARNING_STATE);
        } else {
            completion.put(POS_DETAILS_KEY, true);
            view.changeState(POS_DETAILS_POSITION, MpCompletedStateView.COMPLETED_STATE);
            savePOSDetails(posId, alias, address, password);
        }

    }

    @Override
    public void checkAccountCorrection() {
        if (getAccounts().isEmpty()) {
            completion.put(ACCOUNT_KEY, false);
            view.changeState(ACCOUNT_POSITION, MpCompletedStateView.WARNING_STATE);
            if (completion.get(PAYMENT_TYPE_KEY) != null && completion.get(PAYMENT_TYPE_KEY)) {
                completion.put(PAYMENT_TYPE_KEY, false);
                view.changeState(PAYMENT_TYPE_POSITION, MpCompletedStateView.WARNING_STATE);
            }
        } else {
            completion.put(ACCOUNT_KEY, true);
            view.changeState(ACCOUNT_POSITION, MpCompletedStateView.COMPLETED_STATE);
        }
    }

    @Override
    public void checkCurrencyCorrection() {
        if (getDbCurrencies() == null || getDbCurrencies().isEmpty()) {
            completion.put(CURRENCY_KEY, false);
            view.changeState(CURRENCY_POSITION, MpCompletedStateView.WARNING_STATE);
        } else {
            completion.put(CURRENCY_KEY, true);
            view.changeState(CURRENCY_POSITION, MpCompletedStateView.COMPLETED_STATE);
        }
    }

    @Override
    public void checkPaymentTypeCorrection() {
        if (getPaymentTypes() == null || getPaymentTypes().isEmpty()) {
            completion.put(PAYMENT_TYPE_KEY, false);
            view.changeState(PAYMENT_TYPE_POSITION, MpCompletedStateView.WARNING_STATE);
        } else {
            completion.put(PAYMENT_TYPE_KEY, true);
            view.changeState(PAYMENT_TYPE_POSITION, MpCompletedStateView.COMPLETED_STATE);
        }
    }


    @Override
    public void createCurrency(String name, String abbr) {
        Currency currency = null;
        if (name == null && abbr == null) {
            List<Currency> resCurrencies = getCurrencies();
            if (!resCurrencies.isEmpty()) {
                currency = resCurrencies.get(0);
            }
        } else {
            List<Currency> dbCurrencies = getDbCurrencies();
            if (dbCurrencies == null || dbCurrencies.isEmpty()) {
                currency = new Currency();
                currency.setName(name);
                currency.setAbbr(abbr);
                currency.setActive(true);
                currency.setMain(true);
            } else {
                currency = dbCurrencies.get(0);
                currency.setName(name);
                currency.setAbbr(abbr);
            }
        }
        databaseManager.addCurrency(currency).subscribe(id -> Log.d("sss", "createCurrency: " + id));
    }

    @Override
    public String getCurrencyName() {
        String result = null;
        if (getDbCurrencies() != null && !getDbCurrencies().isEmpty()) {
            result = getDbCurrencies().get(0).getName() + " (" + getDbCurrencies().get(0).getAbbr() + ")";
        }
        return result;
    }

    @Override
    public List<PaymentType> getPaymentTypes() {
        return databaseManager.getAllPaymentTypes().blockingSingle();
    }

    @Override
    public void deletePaymentType(PaymentType paymentType) {
        databaseManager.removePaymentType(paymentType).subscribe(isDeleted -> {
            if (isDeleted) {
                checkPaymentTypeCorrection();
                openPaymentType();
            }
        });

    }

    @Override
    public boolean isAccountNameExists(String name) {
        return databaseManager.getAccountOperations().isAccountNameExists(name).blockingSingle();
    }

    @Override
    public boolean isPaymentTypeNameExists(String name) {
        return databaseManager.isPaymentTypeNameExists(name).blockingSingle();
    }
}
