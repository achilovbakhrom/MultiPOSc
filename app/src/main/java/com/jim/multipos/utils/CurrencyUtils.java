package com.jim.multipos.utils;

import android.content.Context;

import com.jim.multipos.R;
import com.jim.multipos.data.db.model.currency.Currency;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bakhrom on 11/4/17.
 */

public class CurrencyUtils {

    public static List<Currency> generateCurrencies(Context context) {
        List<Currency> result = new ArrayList<>();
        String[] titles = context.getResources().getStringArray(R.array.currency_title);
        String[] abbrs = context.getResources().getStringArray(R.array.currency_abbrs);
        String[] titleWithAbbr = context.getResources().getStringArray(R.array.currency_with_abbr);
        for (int i = 0; i < titles.length; i++) {
            Currency currency = new Currency();
            currency.setName(titles[i]);
            currency.setAbbr(abbrs[i]);
            currency.setIsMain(i == 0);
            result.add(currency);
        }
        return result;
    }

}
