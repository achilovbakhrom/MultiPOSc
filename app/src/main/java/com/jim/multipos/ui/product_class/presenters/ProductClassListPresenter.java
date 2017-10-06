package com.jim.multipos.ui.product_class.presenters;

import com.jim.multipos.core.BaseFragmentPresenter;
import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.ui.product_class.fragments.ProductClassListView;

/**
 * Created by developer on 29.08.2017.
 */

public interface ProductClassListPresenter extends Presenter {
    void pressedAddButton();
    void pressedItem(int pos);
    void onAddProductClass(ProductClass productClass);
    void onUpdateProductClass(ProductClass productClass);
}
