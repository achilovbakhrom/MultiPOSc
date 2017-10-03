package com.jim.multipos.ui.first_configure.fragments;

import com.jim.multipos.data.db.model.currency.Currency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 01.08.17.
 */

public interface CurrencyFragmentView {
    void showOtherCurrencies(List<Currency> currencies);
    void showMainCurrencies(List<Currency> currencies, int position);
    void updateView();
    void showRecyclerView(List<Currency> currencies);
}
