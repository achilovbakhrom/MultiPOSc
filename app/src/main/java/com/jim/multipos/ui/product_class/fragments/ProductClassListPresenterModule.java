package com.jim.multipos.ui.product_class.fragments;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.product_class.presenters.ProductClassListPresenter;
import com.jim.multipos.ui.product_class.presenters.ProductClassListPresenterImpl;

import dagger.Binds;
import dagger.Module;

/**
 * Created by bakhrom on 10/6/17.
 */

@Module
public abstract class ProductClassListPresenterModule {
    @Binds
    @PerFragment
    abstract ProductClassListPresenter provideProductClassListPresenter(ProductClassListPresenterImpl classListPresenter);
}
