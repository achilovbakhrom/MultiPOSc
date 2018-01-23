package com.jim.multipos.ui.mainpospage.view;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.products.Product;

import java.util.List;

/**
 * Created by Sirojiddin on 20.01.2018.
 */

public interface BarcodeScannerView extends BaseView{
    void sendFoundProductToOrder(Long id);
    void openProductActivity();
    void openChooseProductDialog(List<Product> productList);
    void openAddNewProductNotificationDialog();

}
