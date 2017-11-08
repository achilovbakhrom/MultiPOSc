package com.jim.multipos.ui.mainpospage.presenter;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.ui.mainpospage.view.ProductInfoView;

/**
 * Created by Sirojiddin on 07.11.2017.
 */

public class ProductInfoPresenterImpl extends BasePresenterImpl<ProductInfoView> implements ProductInfoPresenter {

    protected ProductInfoPresenterImpl(ProductInfoView productInfoView) {
        super(productInfoView);
    }
}
