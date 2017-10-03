package com.jim.multipos.ui.products.fragments;

import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.unit.Unit;

import java.util.List;

/**
 * Created by DEV on 26.08.2017.
 */

public interface ProductsView {
    void setProductImage(int resId);
    void setUnitItems(List<Unit> unitItems);
    void setClassItems(List<ProductClass> classItems);
    void setCurrencyItems(List<Currency> currencyItems);
    void clearFields();
    void setData();
    void setFields(String name, String barcode, String sku, String price, String cost, int unit, int priceCurrency, int costCurrency, String vendor, int productClass, boolean taxed, boolean active, boolean recipe);
    void openAdvanceOptions();
    void setRecipeState(boolean state);
}
