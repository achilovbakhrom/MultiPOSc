package com.jim.multipos.ui.products_expired.expired_products;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ExpiredProductsFragmentPresenterModule {

    @Binds
    @PerFragment
    abstract ExpiredProductsFragmentPresenter provideExpiredProductsFragmentPresenter (ExpiredProductsFragmentPresenterImpl presenter);
}
