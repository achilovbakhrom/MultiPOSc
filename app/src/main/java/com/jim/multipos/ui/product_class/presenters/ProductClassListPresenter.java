package com.jim.multipos.ui.product_class.presenters;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.ProductClass;

/**
 * Created by developer on 29.08.2017.
 */

public interface ProductClassListPresenter extends Presenter {
    void pressedAddButton();
    void pressedItem(int pos);
    void onAddProductClass(ProductClass productClass);
    void onUpdateProductClass(ProductClass productClass);
    void onDeleteProductClass(ProductClass productClass);
}
