package com.jim.multipos.ui.admin_main_page.fragments.product_class.di;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.admin_main_page.fragments.product_class.ProductClassAddFragment;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ProductClassAddModule {

    @Binds
    @PerFragment
    abstract Fragment provideFragment(ProductClassAddFragment fragment);
}
