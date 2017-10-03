package com.jim.multipos.ui.product_class.presenters;

import com.jim.multipos.ui.product_class.ProductClassView;
import com.jim.multipos.ui.product_class.presenters.ProductClassPresenter;

/**
 * Created by developer on 29.08.2017.
 */

public class ProductClassPresenterImpl implements ProductClassPresenter {

    private ProductClassView view;

    @Override
    public void init(ProductClassView view) {
        this.view = view;
    }

}
