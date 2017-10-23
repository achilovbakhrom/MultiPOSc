package com.jim.multipos.ui.discount.presenters;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.ui.discount.fragments.DiscountAddingView;

import javax.inject.Inject;

/**
 * Created by developer on 23.10.2017.
 */

public class DiscountAddingPresenterImpl extends BasePresenterImpl<DiscountAddingView> implements DiscountAddingPresenter {

    @Inject
    protected DiscountAddingPresenterImpl(DiscountAddingView view) {
        super(view);
    }
}
