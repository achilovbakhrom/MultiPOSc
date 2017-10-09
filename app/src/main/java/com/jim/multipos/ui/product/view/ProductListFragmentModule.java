package com.jim.multipos.ui.product.view;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.product.presenter.ProductListPresenterModule;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Sirojiddin on 09.10.2017.
 */

@Module(includes = {
        ProductListPresenterModule.class
})
public abstract class ProductListFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment provideFragment(ProductsListFragment productsListFragment);

    @Binds
    @PerFragment
    abstract ProductListView provideProductListView(ProductsListFragment productsListFragment);
}
