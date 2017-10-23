package com.jim.multipos.ui.product_class_new.di;


import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.core.BaseActivityModule;
import com.jim.multipos.ui.product_class.fragments.AddProductClassFragment;
import com.jim.multipos.ui.product_class_new.ProductsClassActivity;
import com.jim.multipos.ui.product_class_new.fragments.ProductsClassFragment;
import com.jim.multipos.ui.product_class_new.fragments.ProductsClassPresenterModule;
import com.jim.multipos.ui.product_class_new.presenters.ProductsClassFragmentModule;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by DEV on 27.07.2017.
 */
@Module(includes = BaseActivityModule.class)
public abstract class ProductsClassModule {

    @Binds
    @PerActivity
    abstract AppCompatActivity provideProductsClassActivity(ProductsClassActivity productsClassActivity);

    @PerFragment
    @ContributesAndroidInjector(modules = ProductsClassFragmentModule.class)
    abstract ProductsClassFragment provideProductsClassFragment();


}
