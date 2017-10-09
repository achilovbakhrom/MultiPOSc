package com.jim.multipos.ui.product.presenter;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Sirojiddin on 09.10.2017.
 */
@Module
public abstract class ProductListPresenterModule {
    @Binds
    @PerFragment
    abstract ProductListPresenter provideProductListPresenter(ProductListPresenterImpl productListPresenter);
}
