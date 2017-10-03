package com.jim.multipos.ui.products.presenters;

import com.jim.multipos.core.BaseFragmentPresenter;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.Vendor;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.unit.Unit;
import com.jim.multipos.ui.products.fragments.ProductsView;

/**
 * Created by DEV on 09.08.2017.
 */

public interface ProductsPresenter extends BaseFragmentPresenter<ProductsView> {
    void saveProduct(String s, String toString, String string, String s1, String toString1, Currency priceCurrency, Currency costCurrency, Unit unit, Vendor vendor, ProductClass productClass, boolean isActive, boolean isTaxed, boolean hasRecipe);
    void getCurrencies();
    void getUnits();
    void getProductClass();
    void checkData();
    void onDestroy();
    void onHasRecipeChange(boolean checkboxChecked);
    void onAdvance(String name, String barcode, String sku, String price, String cost, Currency priceCurrency, Currency costCurrency, Unit unit, Vendor vendor, ProductClass productClass, boolean isActive, boolean isTaxed, boolean hasRecipe);
}
