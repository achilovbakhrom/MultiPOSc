package com.jim.multipos.ui.start_configuration.currency;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.prefs.PreferencesHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

public class CurrencyPresenterImpl extends BasePresenterImpl<CurrencyView> implements CurrencyPresenter {

    private DatabaseManager databaseManager;
    private PreferencesHelper preferencesHelper;
    private List<Currency> currencies;
    private int position = 0;

    @Inject
    protected CurrencyPresenterImpl(CurrencyView currencyView, DatabaseManager databaseManager, @Named(value = "curr_name") String[] currencyName,
                                    @Named(value = "curr_abbr") String[] currencyAbbr, PreferencesHelper preferencesHelper) {
        super(currencyView);
        this.databaseManager = databaseManager;
        this.preferencesHelper = preferencesHelper;
        currencies = new ArrayList<>();
        Currency mainCurrency = databaseManager.getMainCurrency();
        for (int i = 0; i < currencyName.length; i++) {
            Currency currency = new Currency();
            if (currencyName[i].equals(mainCurrency.getName())) {
                currency.setIsMain(i == 0);
                position = i;
            }
            currency.setName(currencyName[i]);
            currency.setAbbr(currencyAbbr[i]);
            currency.setActive(true);
            currencies.add(currency);
        }
    }

    @Override
    public void initCurrency() {
        String[] items = new String[currencies.size()];
        for (Currency currency : currencies) {
            items[currencies.indexOf(currency)] = currency.getName() + "(" + currency.getAbbr() + ")";
        }
        view.setItemsToSpinner(items);
        view.setSelection(position);
    }

    @Override
    public void setCurrency(int position) {
        Currency currency = databaseManager.getMainCurrency();
        currency.setName(currencies.get(position).getName());
        currency.setAbbr(currencies.get(position).getAbbr());
        databaseManager.addCurrency(currency).subscribe();
    }

    @Override
    public void setAppRunFirstTimeValue(boolean state) {
        preferencesHelper.setAppRunFirstTimeValue(false);
    }
}
