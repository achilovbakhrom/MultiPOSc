package com.jim.multipos.ui.products_expired;

import com.jim.multipos.core.BasePresenterImpl;

import javax.inject.Inject;

public class ExpiredProductsPresenterImpl extends BasePresenterImpl<ExpiredProductsView> implements ExpiredProductsPresenter {

    @Inject
    protected ExpiredProductsPresenterImpl(ExpiredProductsView expiredProductsView) {
        super(expiredProductsView);
    }
}
