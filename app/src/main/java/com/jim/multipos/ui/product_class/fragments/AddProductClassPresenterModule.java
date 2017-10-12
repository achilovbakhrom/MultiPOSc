package com.jim.multipos.ui.product_class.fragments;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.product_class.presenters.AddProductClassPresenter;
import com.jim.multipos.ui.product_class.presenters.AddProductClassPresenterImpl;

import dagger.Binds;
import dagger.Module;

/**
 * Created by bakhrom on 10/6/17.
 */

@Module
public abstract class AddProductClassPresenterModule {
    @Binds
    @PerFragment
    abstract AddProductClassPresenter provideAddProductClassPresenter(AddProductClassPresenterImpl productClassPresenter);
}
