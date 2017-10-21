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
import com.jim.multipos.data.db.model.unit.Unit;
import com.jim.multipos.data.db.model.unit.UnitCategory;
import com.jim.multipos.data.prefs.PreferencesHelper;
import com.jim.multipos.ui.first_configure.fragments.UnitsFragment;
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
    @Getter
    private final String[] types;
    @Getter
    private final String[] circulations;

    private final String[] currencyName;
    private final String[] currencyAbbr;
    private final String[] baseUnitsTitle;
    private final String[] baseUnitsAbbr;
    private final String[] weightUnitsTitle;
    private final String[] lengthUnitsTitle;
    private final String[] areaUnitsTitle;
    private final String[] volumeUnitsTitle;
    private final String[] weightUnitsAbbr;
    private final String[] lengthUnitsAbbr;
    private final String[] areaUnitsAbbr;
    private final String[] volumeUnitsAbbr;
    private final String[] weightUnitsRootFactor;
    private final String[] lengthUnitsRootFactor;
    private final String[] areaUnitsRootFactor;
    private final String[] volumeUnitsRootFactor;
    private final String[] firstConfigureListItems;
    private final String[] firstConfigurationItemsDescription;
    public static final String COMPLETED_FRAGMENTS_KEY = "COMPLETED_FRAGMENTS_KEY";
    private Map<String, Boolean> completion = new HashMap<>();
    @Inject
    public FirstConfigurePresenterImpl(FirstConfigureView view,
                                       DatabaseManager databaseManager,
                                       PreferencesHelper preferencesHelper,
                                       @Named(value = "account_circulations") String[] circulations,
                                       @Named(value = "account_types") String[] types,
                                       @Named(value = "currency_name") String[] currencyName,
                                       @Named(value = "currency_abbr") String[] currencyAbbr,
                                       @Named(value = "base_units_title") String[] baseUnitsTitle,
                                       @Named(value = "base_units_abbr") String[] baseUnitsAbbr,
                                       @Named(value = "weight_units_title") String[] weightUnitsTitle,
                                       @Named(value = "length_units_title") String[] lengthUnitsTitle,
                                       @Named(value = "area_units_title") String[] areaUnitsTitle,
                                       @Named(value = "volume_units_title") String[] volumeUnitsTitle,
                                       @Named(value = "weight_units_abbr") String[] weightUnitsAbbr,
                                       @Named(value = "length_units_abbr") String[] lengthUnitsAbbr,
                                       @Named(value = "area_units_abbr") String[] areaUnitsAbbr,
                                       @Named(value = "volume_units_abbr") String[] volumeUnitsAbbr,
                                       @Named(value = "weight_units_root_factor") String[] weightUnitsRootFactor,
                                       @Named(value = "length_units_root_factor") String[] lengthUnitsRootFactor,
                                       @Named(value = "area_units_root_factor") String[] areaUnitsRootFactor,
                                       @Named(value = "volume_units_root_factor") String[] volumeUnitsRootFactor,
                                       @Named(value = "first_configure_items") String[] firstConfigureListItems,
                                       @Named(value = "first_configure_items_description") String[] firstConfigurationItemsDescription) {
        super(view);
        this.preferences = preferencesHelper;
        this.databaseManager = databaseManager;
        this.view = view;
        this.completedFragments = new int[5];
        this.types = types;
        this.circulations = circulations;
        this.currencyName = currencyName;
        this.currencyAbbr = currencyAbbr;
        this.baseUnitsTitle = baseUnitsTitle;
        this.baseUnitsAbbr = baseUnitsAbbr;
        this.weightUnitsTitle = weightUnitsTitle;
        this.lengthUnitsTitle = lengthUnitsTitle;
        this.areaUnitsTitle = areaUnitsTitle;
        this.volumeUnitsTitle = volumeUnitsTitle;
        this.weightUnitsAbbr = weightUnitsAbbr;
        this.lengthUnitsAbbr = lengthUnitsAbbr;
        this.areaUnitsAbbr = areaUnitsAbbr;
        this.volumeUnitsAbbr = volumeUnitsAbbr;
        this.weightUnitsRootFactor = weightUnitsRootFactor;
        this.lengthUnitsRootFactor = lengthUnitsRootFactor;
        this.areaUnitsRootFactor = areaUnitsRootFactor;
        this.volumeUnitsRootFactor = volumeUnitsRootFactor;
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
    public void savePOSDetials(String posId, String alias, String address, String password) {
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
    public Account addAccount(String name, int type, int circulation) {
        Account account = new Account();
        account.setName(name);
        account.setType(type);
        account.setCirculation(circulation);
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
            for(PaymentType pt : paymentTypes) {
                if (Objects.equals(pt.getId(), id)) {
                    result = pt;
                    break;
                }
            }
        }
        view.changeState(PAYMENT_TYPE_POSITION ,MpCompletedStateView.COMPLETED_STATE);
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
            savePOSDetials(posId, alias, address, password);
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
        }
        else {
            completion.put(ACCOUNT_KEY, true);
            view.changeState(ACCOUNT_POSITION, MpCompletedStateView.COMPLETED_STATE);
        }
    }

    @Override
    public void checkCurrencyCorrection() {
        if (getDbCurrencies() == null || getDbCurrencies().isEmpty()) {
            completion.put(CURRENCY_KEY, false);
            view.changeState(CURRENCY_POSITION, MpCompletedStateView.WARNING_STATE);
        }
        else {
            completion.put(CURRENCY_KEY, true);
            view.changeState(CURRENCY_POSITION, MpCompletedStateView.COMPLETED_STATE);
        }
    }

    @Override
    public void checkPaymentTypeCorrection() {
        if (getPaymentTypes() == null || getPaymentTypes().isEmpty()) {
            completion.put(PAYMENT_TYPE_KEY, false);
            view.changeState(PAYMENT_TYPE_POSITION, MpCompletedStateView.WARNING_STATE);
        }
        else {
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
    public boolean isPayemntTypeNameExists(String name) {
        return databaseManager.isPaymentTypeNameExists(name).blockingSingle();
    }

    //    @Override
//    public void onCreateView(Bundle bundle) {
//        super.onCreateView(bundle);
//        if (bundle != null) {
//            completedFragments = bundle.getIntArray(COMPLETED_FRAGMENTS_KEY);
//        }
//    }
//
//    @Override
//    public void onSaveInstanceState(@Nullable Bundle bundle) {
//        super.onSaveInstanceState(bundle);
//        bundle.putIntArray(COMPLETED_FRAGMENTS_KEY, completedFragments);
//    }
//
//    @Override
//    public void openNextFragment() {
//        if (completedFragments == null) return;
//
//        int nextPosition = -1;
//        int notCompletedCount = 0;
//
//        for (int i = 0; i < completedFragments.length; i++) {
//            if (completedFragments[i] == MpCompletedStateView.EMPTY_STATE || completedFragments[i] == MpCompletedStateView.WARNING_STATE) {
//                nextPosition = i;
//                break;
//            }
//        }
//
//        for (int i = 0; i < completedFragments.length; i++) {
//            if (completedFragments[i] == MpCompletedStateView.EMPTY_STATE || completedFragments[i] == MpCompletedStateView.WARNING_STATE) {
//                notCompletedCount++;
//            }
//        }
//
//        if (notCompletedCount == 0) {
//            view.closeActivity();
//
//            return;
//        }
//
//        if (nextPosition != -1) {
//            view.replaceFragment(nextPosition);
//            view.updateLeftSideFragment(nextPosition);
//        }
//    }
//
//    @Override
//    public void openNextFragment(int position, int nextPosition) {
//        switch (position) {
//            case POS_DETAIL_FRAGMENT_ID:
//                if (preferences.getPosDetailPosId() == null || preferences.getPosDetailPosId().isEmpty()) {
//                    completedFragments[POS_DETAIL_FRAGMENT_ID] = MpCompletedStateView.WARNING_STATE;
//                } else {
//                    completedFragments[POS_DETAIL_FRAGMENT_ID] = MpCompletedStateView.COMPLETED_STATE;
//                }
//
//                view.replaceFragment(nextPosition);
//                view.updateLeftSideFragment(nextPosition);
//                break;
//            case ACCOUNT_FRAGMENT_ID:
//                databaseManager.getAccountOperations().getAllAccounts().subscribe(accounts -> {
//                    if (accounts.isEmpty()) {
//                        completedFragments[ACCOUNT_FRAGMENT_ID] = MpCompletedStateView.WARNING_STATE;
//                    } else {
//                        completedFragments[ACCOUNT_FRAGMENT_ID] = MpCompletedStateView.COMPLETED_STATE;
//                    }
//
//                    view.replaceFragment(nextPosition);
//                    view.updateLeftSideFragment(nextPosition);
//                });
//                break;
//            case CURRENCY_FRAGMENT_ID:
//                completedFragments[CURRENCY_FRAGMENT_ID] = MpCompletedStateView.COMPLETED_STATE;
//
//                view.replaceFragment(nextPosition);
//                view.updateLeftSideFragment(nextPosition);
//                break;
//            case PAYMENT_TYPE_FRAGMENT_ID:
//                databaseManager.getPaymentTypeOperations().getAllPaymentTypes().subscribe(paymentTypes -> {
//                    if (paymentTypes.isEmpty()) {
//                        completedFragments[PAYMENT_TYPE_FRAGMENT_ID] = MpCompletedStateView.WARNING_STATE;
//                    } else {
//                        completedFragments[PAYMENT_TYPE_FRAGMENT_ID] = MpCompletedStateView.COMPLETED_STATE;
//                    }
//
//                    view.replaceFragment(nextPosition);
//                    view.updateLeftSideFragment(nextPosition);
//                });
//                break;
//            case UNITS_FRAGMENT_ID:
//                completedFragments[UNITS_FRAGMENT_ID] = MpCompletedStateView.COMPLETED_STATE;
//
//                view.replaceFragment(nextPosition);
//                view.updateLeftSideFragment(nextPosition);
//                break;
//        }
//    }
//
//    @Override
//    public void openPrevFragment(int position) {
//        switch (position) {
//            case POS_DETAIL_FRAGMENT_ID:
//                if (preferences.getPosDetailPosId() == null || preferences.getPosDetailPosId().isEmpty()) {
//                    completedFragments[POS_DETAIL_FRAGMENT_ID] = MpCompletedStateView.WARNING_STATE;
//                } else {
//                    completedFragments[POS_DETAIL_FRAGMENT_ID] = MpCompletedStateView.COMPLETED_STATE;
//                }
//                break;
//            case ACCOUNT_FRAGMENT_ID:
//                databaseManager.getAccountOperations().getAllAccounts().subscribe(accounts -> {
//                    if (accounts.isEmpty()) {
//                        completedFragments[ACCOUNT_FRAGMENT_ID] = MpCompletedStateView.WARNING_STATE;;
//                    } else {
//                        completedFragments[ACCOUNT_FRAGMENT_ID] = MpCompletedStateView.COMPLETED_STATE;;
//                    }
//                });
//                break;
//            case CURRENCY_FRAGMENT_ID:
//                completedFragments[CURRENCY_FRAGMENT_ID] = MpCompletedStateView.COMPLETED_STATE;
//                break;
//            case PAYMENT_TYPE_FRAGMENT_ID:
//                databaseManager.getPaymentTypeOperations().getAllPaymentTypes().subscribe(paymentTypes -> {
//                    if (paymentTypes.isEmpty()) {
//                        completedFragments[PAYMENT_TYPE_FRAGMENT_ID] = MpCompletedStateView.WARNING_STATE;
//                    } else {
//                        completedFragments[PAYMENT_TYPE_FRAGMENT_ID] = MpCompletedStateView.COMPLETED_STATE;
//                    }
//                });
//                break;
//            case UNITS_FRAGMENT_ID:
//                completedFragments[UNITS_FRAGMENT_ID] = MpCompletedStateView.COMPLETED_STATE;
//                break;
//        }
//
//        view.openPrevFragment();
//    }
//
//    @Override
//    public boolean isNextButton() {
//        if (completedFragments == null) return false;
//        int i = 0;
//        for (int isCompleted : completedFragments) {
//            if (isCompleted == MpCompletedStateView.EMPTY_STATE || isCompleted == MpCompletedStateView.WARNING_STATE)
//                i++;
//            if (i > 1) return true;
//        }
//        return false;
//    }
//
//    @Override
//    public void fillPosDetailsData(EditText etPosId, EditText etAlias, EditText etAddress, EditText etPassword, EditText etRepeatPassword) {
//        etPosId.setText(preferences.getPosDetailPosId());
//        etAlias.setText(preferences.getPosDetailAlias());
//        etAddress.setText(preferences.getPosDetailAddress());
//        etPassword.setText(preferences.getPosDetailPassword());
//        etRepeatPassword.setText(preferences.getPosDetailPassword());
//    }
//
//    @Override
//    public void fillAccountsRV(RecyclerView recyclerView) {
//        databaseManager.getAccountOperations().getAllAccounts().subscribe(accounts -> {
//            recyclerView.setAdapter(new SystemAccountsAdapter(accounts, types, circulations, account -> {
//                databaseManager.getAccountOperations().removeAccount(account).subscribe(aBoolean -> {
//                    view.removeAccountItem(account);
//                });
//            }));
//        });
//    }
//
//    @Override
//    public void fillPaymentTypesRV(RecyclerView recyclerView) {
//        databaseManager.getPaymentTypeOperations().getAllPaymentTypes().subscribe(paymentTypes -> {
//            recyclerView.setAdapter(new SystemPaymentTypesAdapter(paymentTypes, paymentType -> {
//                databaseManager.getPaymentTypeOperations().removePaymentType(paymentType).subscribe(aBoolean -> {
//                    view.removePaymentTypeItem(paymentType);
//                });
//            }));
//        });
//    }
//
//    @Override
//    public void setupCurrencyData() {
//        List<Currency> currencies = new ArrayList<>();
//
//        for (int i = 0; i < currencyName.length; i++) {
//            Currency currency = new Currency();
//            currency.setName(currencyName[i]);
//            currency.setAbbr(currencyAbbr[i]);
//
//            currencies.add(currency);
//        }
//
//        databaseManager.getCurrencyOperations().getAllCurrencies().subscribe(dbCurrencies -> {
//            int position = 0;
//
//
//            if (!dbCurrencies.isEmpty()) {
//                for (int i = 0; i < currencies.size(); i++) {
//                    if (currencies.get(i).getAbbr().equals(dbCurrencies.get(0).getAbbr())) {
//                        currencies.get(i).setId(dbCurrencies.get(0).getId());
//                        position = i;
//                    }
//                }
//            } else {
//                databaseManager.getCurrencyOperations().addCurrency(currencies.get(position)).subscribe();
//            }
//
//            view.setCurrencySpinnerData(currencies, position);
//        });
//    }
//
//    @Override
//    public void changeCurrency(Currency currency) {
//        databaseManager.getCurrencyOperations().removeAllCurrencies().subscribe(aBoolean -> {
//            databaseManager.getCurrencyOperations().addCurrency(currency).subscribe();
//        });
//    }
//

//
//    @Override
//    public void addPaymentType(String name, Account account) {
//        databaseManager.getCurrencyOperations().getAllCurrencies().subscribe(currencies -> {
//            PaymentType paymentType = new PaymentType();
//            paymentType.setName(name);
//            paymentType.setAccount(account);
//            paymentType.setCurrency(currencies.get(0));
//
//            databaseManager.getPaymentTypeOperations().addPaymentType(paymentType).subscribe(aLong -> {
//                view.addPaymentTypeItem(paymentType);
//            });
//        });
//
//    }
//
//    @Override
//    public Boolean isAccountNameExists(String name) {
//        return databaseManager.getAccountOperations().isAccountNameExists(name);
//    }
//
//    @Override
//    public Boolean isPaymentTypeNameExists(String name) {
//        return databaseManager.getPaymentTypeOperations().isPaymentTypeNameExists(name);
//    }
//
//    @Override
//    public void checkAccountData() {
//        databaseManager.getAccountOperations().getAllAccounts().subscribe(accounts -> {
//            if (accounts.size() > 0) {
//                completedFragments[ACCOUNT_FRAGMENT_ID] = MpCompletedStateView.COMPLETED_STATE;
//                openNextFragment();
//            } else {
//                view.showAccountToast();
//                completedFragments[ACCOUNT_FRAGMENT_ID] = MpCompletedStateView.WARNING_STATE;
//            }
//        });
//    }
//
//    @Override
//    public void checkPaymentTypeData() {
//        databaseManager.getPaymentTypeOperations().getAllPaymentTypes().subscribe(paymentTypes -> {
//            if (paymentTypes.isEmpty()) {
//                view.showPaymentTypeToast();
//                completedFragments[PAYMENT_TYPE_FRAGMENT_ID] = MpCompletedStateView.WARNING_STATE;
//            } else {
//                completedFragments[PAYMENT_TYPE_FRAGMENT_ID] = MpCompletedStateView.COMPLETED_STATE;
//                openNextFragment();
//            }
//        });
//    }
//
//    @Override
//    public void savePosDetailsData(String posId, String alias, String address, String password) {
//        preferences.setPosDetailPosId(posId);
//        preferences.setPosDetailAlias(alias);
//        preferences.setPosDetailAddress(address);
//        preferences.setPosDetailPassword(password);
//    }
//
//    @Override
//    public void setupPaymentTypeCurrency() {
//        databaseManager.getCurrencyOperations().getAllCurrencies().subscribe(currencies -> {
//            if (currencies.isEmpty()) {
//                view.showPaymentTypeCurrencyToast();
//            } else {
//                view.setPaymentTypeCurrency(currencies.get(0));
//            }
//        });
//    }
//
//    @Override
//    public void setupPaymentTypeAccount() {
//        databaseManager.getAccountOperations().getAllAccounts().subscribe(accounts -> {
//            if (accounts.isEmpty()) {
//                view.showPaymentTypeAccountToast();
//            }
//
//            view.setPaymentTypeAccount(accounts);
//        });
//    }
//

//    public void setupUnits(RecyclerView rvWeight, RecyclerView rvLength, RecyclerView rvArea, RecyclerView rvVolume) {
//        databaseManager.getUnitCategoryOperations().getAllUnitCategories().subscribe(categories -> {
//            if (categories.isEmpty()) {
//                databaseManager.getUnitCategoryOperations().addUnitCategories(createUnitCategories()).subscribe(aBoolean -> {
//                    databaseManager.getUnitCategoryOperations().getAllUnitCategories().subscribe(categories1 -> {
//                        if (!categories1.isEmpty()) {
//                            databaseManager.getUnitOperations().addUnits(createUnits(categories1)).subscribe(aBoolean1 -> {
//                                databaseManager.getUnitOperations().getUnits(categories1.get(0).getId(), categories1.get(0).getName()).subscribe(units -> {
//                                    rvWeight.setAdapter(new UnitAdapter(units, new UnitAdapter.OnClickListener() {
//                                        @Override
//                                        public void addUnitItem(Unit item) {
//                                            databaseManager.getUnitOperations().updateUnit(item).subscribe(aLong -> {
//                                                view.addWeightUnit(item);
//                                            });
//                                        }
//
//                                        @Override
//                                        public void removeUnitItem(Unit item) {
//                                            databaseManager.getUnitOperations().updateUnit(item).subscribe(unit -> {
//                                                view.removeWeightUnit(item);
//                                            });
//                                        }
//                                    }));
//                                });
//                                databaseManager.getUnitOperations().getUnits(categories1.get(1).getId(), categories1.get(1).getName()).subscribe(units -> {
//                                    rvLength.setAdapter(new UnitAdapter(units, new UnitAdapter.OnClickListener() {
//                                        @Override
//                                        public void addUnitItem(Unit item) {
//                                            databaseManager.getUnitOperations().updateUnit(item).subscribe(aLong -> {
//                                                view.addLengthUnit(item);
//                                            });
//                                        }
//
//                                        @Override
//                                        public void removeUnitItem(Unit item) {
//                                            databaseManager.getUnitOperations().updateUnit(item).subscribe(aLong -> {
//                                                view.removeLengthUnit(item);
//                                            });
//                                        }
//                                    }));
//                                });
//                                databaseManager.getUnitOperations().getUnits(categories1.get(2).getId(), categories1.get(2).getName()).subscribe(units -> {
//                                    rvArea.setAdapter(new UnitAdapter(units, new UnitAdapter.OnClickListener() {
//                                        @Override
//                                        public void addUnitItem(Unit item) {
//                                            databaseManager.getUnitOperations().updateUnit(item).subscribe(aLong -> {
//                                                view.addAreaUnit(item);
//                                            });
//                                        }
//
//                                        @Override
//                                        public void removeUnitItem(Unit item) {
//                                            databaseManager.getUnitOperations().updateUnit(item).subscribe(aLong -> {
//                                                view.removeAreaUnit(item);
//                                            });
//                                        }
//                                    }));
//                                });
//                                databaseManager.getUnitOperations().getUnits(categories1.get(3).getId(), categories1.get(3).getName()).subscribe(units -> {
//                                    rvVolume.setAdapter(new UnitAdapter(units, new UnitAdapter.OnClickListener() {
//                                        @Override
//                                        public void addUnitItem(Unit item) {
//                                            databaseManager.getUnitOperations().updateUnit(item).subscribe(aLong -> {
//                                                view.addVolumeUnit(item);
//                                            });
//                                        }
//
//                                        @Override
//                                        public void removeUnitItem(Unit item) {
//                                            databaseManager.getUnitOperations().updateUnit(item).subscribe(aLong -> {
//                                                view.removeVolumeUnit(item);
//                                            });
//                                        }
//                                    }));
//                                });
//                            });
//                        }
//                    });
//                });
//            } else {
//                databaseManager.getUnitCategoryOperations().getAllUnitCategories().subscribe(categories1 -> {
//                    if (!categories1.isEmpty()) {
//                        databaseManager.getUnitOperations().getUnits(categories1.get(0).getId(), categories1.get(0).getName()).subscribe(units -> {
//                            rvWeight.setAdapter(new UnitAdapter(units, new UnitAdapter.OnClickListener() {
//                                @Override
//                                public void addUnitItem(Unit item) {
//                                    databaseManager.getUnitOperations().updateUnit(item).subscribe(aLong -> {
//                                        view.addWeightUnit(item);
//                                    });
//                                }
//
//                                @Override
//                                public void removeUnitItem(Unit item) {
//                                    databaseManager.getUnitOperations().updateUnit(item).subscribe(aLong -> {
//                                        view.removeWeightUnit(item);
//                                    });
//                                }
//                            }));
//                        });
//                        databaseManager.getUnitOperations().getUnits(categories1.get(1).getId(), categories1.get(1).getName()).subscribe(units -> {
//                            rvLength.setAdapter(new UnitAdapter(units, new UnitAdapter.OnClickListener() {
//                                @Override
//                                public void addUnitItem(Unit item) {
//                                    databaseManager.getUnitOperations().updateUnit(item).subscribe(aLong -> {
//                                        view.addLengthUnit(item);
//                                    });
//                                }
//
//                                @Override
//                                public void removeUnitItem(Unit item) {
//                                    databaseManager.getUnitOperations().updateUnit(item).subscribe(aLong -> {
//                                        view.removeLengthUnit(item);
//                                    });
//                                }
//                            }));
//                        });
//                        databaseManager.getUnitOperations().getUnits(categories1.get(2).getId(), categories1.get(2).getName()).subscribe(units -> {
//                            rvArea.setAdapter(new UnitAdapter(units, new UnitAdapter.OnClickListener() {
//                                @Override
//                                public void addUnitItem(Unit item) {
//                                    databaseManager.getUnitOperations().updateUnit(item).subscribe(aLong -> {
//                                        view.addAreaUnit(item);
//                                    });
//                                }
//
//                                @Override
//                                public void removeUnitItem(Unit item) {
//                                    databaseManager.getUnitOperations().updateUnit(item).subscribe(aLong -> {
//                                        view.removeAreaUnit(item);
//                                    });
//                                }
//                            }));
//                        });
//                        databaseManager.getUnitOperations().getUnits(categories1.get(3).getId(), categories1.get(3).getName()).subscribe(units -> {
//                            rvVolume.setAdapter(new UnitAdapter(units, new UnitAdapter.OnClickListener() {
//                                @Override
//                                public void addUnitItem(Unit item) {
//                                    databaseManager.getUnitOperations().updateUnit(item).subscribe(aLong -> {
//                                        view.addVolumeUnit(item);
//                                    });
//                                }
//
//                                @Override
//                                public void removeUnitItem(Unit item) {
//                                    databaseManager.getUnitOperations().updateUnit(item).subscribe(aLong -> {
//                                        view.removeVolumeUnit(item);
//                                    });
//                                }
//                            }));
//                        });
//                    }
//                });
//            }
//        });
//    }



//    private List<UnitCategory> createUnitCategories() {
//        List<UnitCategory> unitCategories = new ArrayList<>();
//        for (int i = 0; i < baseUnitsTitle.length; i++) {
////            unitCategories.add(new UnitCategory(baseUnitsTitle[0], baseUnitsAbbr[0]));
//        }
//        return unitCategories;
//    }
//
//    private List<Unit> createUnits(List<UnitCategory> unitCategories) {
//        List<Unit> units = new ArrayList<>();
//        for (int i = 0; i < weightUnitsTitle.length; i++) {
//            Unit unit = new Unit();
//            unit.setName(weightUnitsTitle[i]);
//            unit.setAbbr(weightUnitsAbbr[i]);
//            unit.setFactorRoot(Float.parseFloat(weightUnitsRootFactor[i]));
////            unit.setUnitCategory(unitCategories.get(0));
//            unit.setIsActive(false);
//            unit.setIsStaticUnit(true);
//            units.add(unit);
//        }
//
//        for (int i = 0; i < lengthUnitsTitle.length; i++) {
//            Unit unit = new Unit();
//            unit.setName(lengthUnitsTitle[i]);
//            unit.setAbbr(lengthUnitsAbbr[i]);
//            unit.setFactorRoot(Float.parseFloat(lengthUnitsRootFactor[i]));
////            unit.setUnitCategory(unitCategories.get(1));
//            unit.setIsActive(false);
//            unit.setIsStaticUnit(true);
//            units.add(unit);
//        }
//
//        for (int i = 0; i < areaUnitsTitle.length; i++) {
//            Unit unit = new Unit();
//            unit.setName(areaUnitsTitle[i]);
//            unit.setAbbr(areaUnitsAbbr[i]);
//            unit.setFactorRoot(Float.parseFloat(areaUnitsRootFactor[i]));
////            unit.setUnitCategory(unitCategories.get(2));
//            unit.setIsActive(false);
//            unit.setIsStaticUnit(true);
//
//            units.add(unit);
//        }
//
//        for (int i = 0; i < volumeUnitsTitle.length; i++) {
//            Unit unit = new Unit();
//            unit.setName(volumeUnitsTitle[i]);
//            unit.setAbbr(volumeUnitsAbbr[i]);
//            unit.setFactorRoot(Float.parseFloat(volumeUnitsRootFactor[i]));
////            unit.setUnitCategory(unitCategories.get(3));
//            unit.setIsActive(false);
//            unit.setIsStaticUnit(true);
//
//            units.add(unit);
//        }
//
//        for (int i = 0; i < unitCategories.size(); i++) {
//            Unit unit = new Unit();
//            unit.setName(unitCategories.get(i).getName());
//            unit.setAbbr(unitCategories.get(i).getAbbr());
//            unit.setFactorRoot(1);
////            unit.setUnitCategory(unitCategories.get(i));
//            unit.setIsActive(true);
//            unit.setIsStaticUnit(true);
//
//            units.add(unit);
//        }
//
//        return units;
//    }



//    @Override
//    public void setCompletedFragments(int state, int position) {
//        completedFragments[position] = state;
//    }
}
