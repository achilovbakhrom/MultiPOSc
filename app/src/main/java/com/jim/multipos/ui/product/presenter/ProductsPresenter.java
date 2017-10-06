package com.jim.multipos.ui.product.presenter;

import com.jim.multipos.core.BaseFragmentPresenter;
import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.Vendor;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.unit.Unit;
import com.jim.multipos.ui.product.view.ProductsView;

/**
 * Created by DEV on 09.08.2017.
 */

public interface ProductsPresenter extends Presenter {
    void saveProduct(String name, String barcode, String sku, String price, String cost, Currency priceCurrency, Currency costCurrency, Unit unit, Vendor vendor, ProductClass productClass, boolean isActive, String photoPath);
    void getCurrencies();
    void getUnits();
    void getProductClass();
    void checkData();
    void onDestroy();
    void onAdvance(String name, String barcode, String sku, String price, String cost, Currency priceCurrency, Currency costCurrency, Unit unit, Vendor vendor, ProductClass productClass, boolean isActive, String photoPath);
}
