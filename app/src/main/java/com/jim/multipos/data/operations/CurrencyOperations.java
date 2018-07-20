package com.jim.multipos.data.operations;

import com.jim.multipos.data.db.model.currency.Currency;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by user on 18.08.17.
 */

public interface CurrencyOperations {
    Observable<Long> addCurrency(Currency currency);

    Observable<Boolean> addCurrencies(List<Currency> currencies);

    Observable<Boolean> removeCurrency(Currency currency);

    Observable<Boolean> removeAllCurrencies();

    Observable<List<Currency>> getAllCurrencies();

    List<Currency> getCurrencies();

    Currency getMainCurrency();
}
