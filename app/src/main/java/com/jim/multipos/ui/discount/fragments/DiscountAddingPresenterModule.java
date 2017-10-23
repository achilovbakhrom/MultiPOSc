package com.jim.multipos.ui.discount.fragments;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.discount.presenters.DiscountAddingPresenter;
import com.jim.multipos.ui.discount.presenters.DiscountAddingPresenterImpl;
import com.jim.multipos.ui.product_class_new.presenters.ProductsClassPresenter;
import com.jim.multipos.ui.product_class_new.presenters.ProductsClassPresenterImpl;

import dagger.Binds;
import dagger.Module;

/**
 * Created by developer on 23.10.2017.
 */
@Module
public abstract class DiscountAddingPresenterModule {
    @Binds
    @PerFragment
    abstract DiscountAddingPresenter provideDiscountAddingPresenter(DiscountAddingPresenterImpl discountAddingPresenter);

}
