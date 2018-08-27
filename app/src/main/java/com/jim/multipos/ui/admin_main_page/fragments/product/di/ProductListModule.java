package com.jim.multipos.ui.admin_main_page.fragments.product.di;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.admin_main_page.fragments.product.ProductsFragment;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ProductListModule {

    @Binds
    @PerFragment
    abstract Fragment provideFragment(ProductsFragment fragment);

}
