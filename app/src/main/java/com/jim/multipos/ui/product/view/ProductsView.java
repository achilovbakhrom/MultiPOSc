package com.jim.multipos.ui.product.view;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.unit.Unit;

import java.util.List;

/**
 * Created by DEV on 26.08.2017.
 */

public interface ProductsView extends BaseView {
    void setUnitItems(List<Unit> unitItems);
    void setClassItems(List<ProductClass> classItems);
    void setCurrencyItems(List<Currency> currencyItems);
    void clearFields();
    void setData();
    void setFields(String name, String barcode, String sku, String price, String cost, int unit, int priceCurrency, int costCurrency, String vendor, int productClass, boolean active, String photoPath);
    void openAdvanceOptions();
}
