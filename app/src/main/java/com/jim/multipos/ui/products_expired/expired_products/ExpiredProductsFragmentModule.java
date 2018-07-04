package com.jim.multipos.ui.products_expired.expired_products;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

@Module(includes = ExpiredProductsFragmentPresenterModule.class)
public abstract class ExpiredProductsFragmentModule {

    @Binds
    @PerFragment
    abstract Fragment provideFragment(ExpiredProductsFragment fragment);

    @Binds
    @PerFragment
    abstract ExpiredProductsFragmentView provideExpiredProductsFragmentView(ExpiredProductsFragment productsFragment);
}
