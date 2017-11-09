package com.jim.multipos.utils;

import android.content.Context;

import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.unit.Unit;
import com.jim.multipos.data.db.model.unit.UnitCategory;

import java.util.List;

/**
 * Created by Achilov Bakhrom on 11/3/17.
 */

public class TestUtils {

    public static void createUnits(DatabaseManager databaseManager, Context context) {
        if (databaseManager.getAllUnitCategories().blockingSingle().isEmpty()) {
            UnitUtils.generateUnitCategories(context, databaseManager);
        }
    }

    public static void createCurrencies(DatabaseManager databaseManager, Context context) {
        if (databaseManager.getCurrencies().isEmpty()) {
            List<Currency> currencies = CurrencyUtils.generateCurrencies(context);
            if (!currencies.isEmpty()) {
                Currency currency = currencies.get(0);
                databaseManager.addCurrency(currency).subscribe();
            }
        }
    }

    public static void createProductClasses(DatabaseManager databaseManager) {

    }

}
