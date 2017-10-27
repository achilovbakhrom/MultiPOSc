package com.jim.multipos.ui.mainpospage.presenter;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.ui.mainpospage.view.ProductPickerView;

/**
 * Created by Sirojiddin on 27.10.2017.
 */

public class ProductPickerPresenterImpl extends BasePresenterImpl<ProductPickerView> implements ProductPickerPresenter {

    protected ProductPickerPresenterImpl(ProductPickerView view) {
        super(view);
    }
}
