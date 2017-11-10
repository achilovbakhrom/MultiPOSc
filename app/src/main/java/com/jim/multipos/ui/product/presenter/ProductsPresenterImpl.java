package com.jim.multipos.ui.product.presenter;


import android.os.Bundle;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.Vendor;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.unit.Unit;
import com.jim.multipos.data.operations.CurrencyOperations;
import com.jim.multipos.data.operations.ProductOperations;
import com.jim.multipos.data.operations.UnitOperations;
import com.jim.multipos.ui.product.view.ProductsView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by DEV on 18.08.2017.
 */
@PerFragment
public class ProductsPresenterImpl extends BasePresenterImpl<ProductsView> implements ProductsPresenter {
    private ProductsView view;
    private DatabaseManager databaseManager;
    private ProductOperations productOperations;
    private CurrencyOperations currencyOperations;
    private UnitOperations unitOperations;
    private Product product;
    //private SubCategory subCategory;
    private final static String PRODUCT_OPENED = "product";
    private final static String OPEN_ADVANCE = "open_advance";
    private static final String ADD = "added";
    private static final String UPDATE = "update";
    private List<Unit> units;
    private List<ProductClass> productClasses;
    private List<Currency> currencies;

    @Inject
    public ProductsPresenterImpl(ProductsView view, DatabaseManager databaseManager) {
        super(view);
        this.databaseManager = databaseManager;
        productOperations = databaseManager.getProductOperations();
        currencyOperations = databaseManager.getCurrencyOperations();
        unitOperations = databaseManager.getUnitOperations();
        currencies = new ArrayList<>();
        units = new ArrayList<>();
        productClasses = new ArrayList<>();
    }

    @Override
    public void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);
        checkData();
    }

    @Override
    public void saveProduct(String name, String barcode, String sku, String price, String cost, Currency priceCurrency, Currency costCurrency, Unit unit, Vendor vendor, ProductClass productClass, boolean isActive, String photoPath) {
        if (this.product == null) {
            product = new Product();
            product.setName(name);
            product.setBarcode(barcode);
            product.setSku(sku);
            product.setPrice(Double.parseDouble(price));
            product.setCost(Double.parseDouble(cost));
            product.setMainUnit(unit);
            product.setMainUnitId(unit.getId());
//            product.setVendor(vendor);
            product.setVendorId(vendor.getId());
            product.setProductClass(productClass);
            product.setClassId(productClass.getId());
            product.setPriceCurrencyId(priceCurrency.getId());
            product.setPriceCurrency(priceCurrency);
            product.setCostCurrency(costCurrency);
            product.setCostCurrencyId(costCurrency.getId());
            product.setActive(isActive);
            //product.setSubCategoryId(subCategory.getId());
            productOperations.addProduct(product).subscribe(aLong -> {
                view.clearFields();
            });}
            else {
            Product newProduct = new Product();
//            product.setActive(false);
//            productOperations.replaceProduct(product);
            newProduct.setName(name);
            newProduct.setBarcode(barcode);
            newProduct.setSku(sku);
            newProduct.setPrice(Double.parseDouble(price));
            newProduct.setCost(Double.parseDouble(cost));
            newProduct.setMainUnit(unit);
            newProduct.setMainUnitId(unit.getId());
//            newProduct.setVendor(vendor);
            newProduct.setVendorId(vendor.getId());
            newProduct.setClassId(productClass.getId());
            newProduct.setProductClass(productClass);
            newProduct.setPriceCurrencyId(priceCurrency.getId());
            newProduct.setPriceCurrency(priceCurrency);
            newProduct.setCostCurrency(costCurrency);
            newProduct.setCostCurrencyId(costCurrency.getId());
            newProduct.setActive(isActive);
            /*newProduct.setSubCategoryId(subCategory.getId());
                productOperations.addProduct(newProduct).subscribe(aLong -> {
//                    rxBus.send(new ProductEvent(newProduct, UPDATE));
                });*/
        }
    }

    @Override
    public void getCurrencies() {
        currencyOperations.getAllCurrencies().subscribe(currencies -> {
            this.currencies = currencies;
            view.setCurrencyItems(currencies);
        });
    }

    @Override
    public void getUnits() {
        unitOperations.getAllStaticUnits().subscribe(units -> {
            this.units = units;
            view.setUnitItems(units);
        });
    }

    @Override
    public void getProductClass() {
        databaseManager.getAllProductClass().subscribe(productClasses -> {
            this.productClasses = productClasses;
            view.setClassItems(productClasses);
        });

    }

    @Override
    public void checkData() {
        getCurrencies();
        getProductClass();
        getUnits();
        if (this.product != null) {
            int priceCurrencyPosition = 0;
            int costCurrencyPosition = 0;
            int unitPosition = 0;
            int classPosition = 0;
            for (int i = 0; i < currencies.size(); i++) {
                if (currencies.get(i).getId().equals(product.getPriceCurrencyId())) {
                    priceCurrencyPosition = i;
                }
                if (currencies.get(i).getId().equals(product.getCostCurrencyId())) {
                    costCurrencyPosition = i;
                }
            }
            for (int i = 0; i < units.size(); i++) {
                if (units.get(i).getId().equals(product.getMainUnitId())) {
                    unitPosition = i;
                }
            }
            for (int i = 0; i < productClasses.size(); i++) {
                if (productClasses.get(i).getId().equals(product.getMainUnitId())) {
                    classPosition = i;
                }
            }
            /*view.setFields(product.getName(), product.getBarcode(), product.getSku(), String.valueOf(product.getPrice()), String.valueOf(product.getCost()),
                    unitPosition, priceCurrencyPosition, costCurrencyPosition, null, classPosition, product.getActive(), product.getPhotoPath());*/
        } else view.clearFields();
    }

    @Override
    public void onDestroy() {
        this.view = null;
    }

    @Override
    public void onAdvance(String name, String barcode, String sku, String price, String cost, Currency priceCurrency, Currency costCurrency, Unit unit, Vendor vendor, ProductClass productClass, boolean isActive, String photoPath) {

    }

    @Override
    public void saveProduct(String name, String barcode, String sku, String price, String cost, Currency priceCurrency, Currency costCurrency, Unit unit, Vendor vendor, ProductClass productClass, boolean checkboxChecked) {

    }

//    @Override
//    void advancedOptionsOpened() {
//        if (product != null) {
//            rxBusLocal.send(new ProductEvent(product, OPEN_ADVANCE));
//        } else if (productNew != null) {
//            rxBusLocal.send(new ProductEvent(productNew, OPEN_ADVANCE));
//        }
//    }
//
//    @Override
//    protected void saveProduct(Product product) {
//        if (!product.getName().isEmpty()) {
//            productOperations.replaceProduct(product).subscribe(aLong -> {
//                rxBus.send(new ProductEvent(product, UPDATE));
//            });
//        } else {
//            this.product = product;
//        }
//    }
//
//    @Override
//    protected void setParentSubCategory(SubCategory subCategory) {
//        this.subCategory = subCategory;
//    }
//
//    @Override
//    protected void setProduct(Product product) {
//        this.product = product;
//        if (isVisible && product == null) {
//            view.clearFields();
//        } else if (isVisible && product != null) {
//            checkData();
//        }
//    }

}
