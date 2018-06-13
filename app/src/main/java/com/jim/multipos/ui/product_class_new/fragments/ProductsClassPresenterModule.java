package com.jim.multipos.ui.product_class_new.fragments;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.product_class_new.presenters.ProductsClassPresenter;
import com.jim.multipos.ui.product_class_new.presenters.ProductsClassPresenterImpl;

import dagger.Binds;
import dagger.Module;

/**
 * Created by developer on 17.10.2017.
 */
@Module
public abstract class ProductsClassPresenterModule {
    @Binds
    @PerFragment
    abstract ProductsClassPresenter provideProductsClassPresenter(ProductsClassPresenterImpl productsClassPresenter);


}
