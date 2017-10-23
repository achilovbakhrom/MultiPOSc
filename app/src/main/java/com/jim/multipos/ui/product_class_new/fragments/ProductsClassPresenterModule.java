package com.jim.multipos.ui.product_class_new.fragments;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.product_class_new.adapters.ProductsClassListAdapter;
import com.jim.multipos.ui.product_class_new.presenters.ProductsClassPresenter;
import com.jim.multipos.ui.product_class_new.presenters.ProductsClassPresenterImpl;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * Created by developer on 17.10.2017.
 */
@Module
public abstract class ProductsClassPresenterModule {
    @Binds
    @PerFragment
    abstract ProductsClassPresenter provideProductsClassPresenter(ProductsClassPresenterImpl productsClassPresenter);


}
