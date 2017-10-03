package com.jim.multipos.ui.product_class.presenters;

import com.jim.multipos.core.BaseFragmentPresenter;
import com.jim.multipos.ui.product_class.fragments.ProductClassListView;

/**
 * Created by developer on 29.08.2017.
 */

public interface ProductClassListPresenter extends BaseFragmentPresenter<ProductClassListView> {
    void pressedAddButton();
    void pressedItem(int pos);
}
