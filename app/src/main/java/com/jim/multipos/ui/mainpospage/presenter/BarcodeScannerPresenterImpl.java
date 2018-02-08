package com.jim.multipos.ui.mainpospage.presenter;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.prefs.PreferencesHelper;
import com.jim.multipos.ui.mainpospage.view.BarcodeScannerView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Sirojiddin on 20.01.2018.
 */

public class BarcodeScannerPresenterImpl extends BasePresenterImpl<BarcodeScannerView> implements BarcodeScannerPresenter {

    private DatabaseManager databaseManager;
    private final PreferencesHelper preferencesHelper;
    private List<Product> productList;
    private boolean showMode;

    @Inject
    protected BarcodeScannerPresenterImpl(BarcodeScannerView view, DatabaseManager databaseManager, PreferencesHelper preferencesHelper) {
        super(view);
        this.databaseManager = databaseManager;
        this.preferencesHelper = preferencesHelper;
        productList = new ArrayList<>();
    }

    @Override
    public void findProductByBarcode(String barcode) {
        showMode = preferencesHelper.isShown();
        databaseManager.getAllProducts().subscribe(products -> {
            productList.clear();
            for (Product product : products) {
                if (product.getBarcode().equals(barcode)) {
                    productList.add(product);
                }
            }

            if (productList.size() == 0) {
                if (!showMode)
                    view.openAddNewProductNotificationDialog();
            } else if (productList.size() == 1) {
                view.sendFoundProductToOrder(productList.get(0).getId());
            } else {
                view.openChooseProductDialog(productList);
            }

        });
    }
}
