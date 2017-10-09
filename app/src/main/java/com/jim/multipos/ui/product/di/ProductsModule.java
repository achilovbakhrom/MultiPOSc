package com.jim.multipos.ui.product.di;

import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.core.BaseActivityModule;
import com.jim.multipos.ui.product.ProductsActivity;
import com.jim.multipos.ui.product.view.AddCategoryFragment;
import com.jim.multipos.ui.product.view.AddCategoryFragmentModule;
import com.jim.multipos.ui.product.view.AddProductFragment;
import com.jim.multipos.ui.product.view.AddProductFragmentModule;
import com.jim.multipos.ui.product.view.AddSubCategoryFragment;
import com.jim.multipos.ui.product.view.AddSubCategoryFragmentModule;
import com.jim.multipos.ui.product.view.ProductListFragmentModule;
import com.jim.multipos.ui.product.view.ProductsListFragment;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by DEV on 09.08.2017.
 */
@Module(includes = BaseActivityModule.class)
public abstract class ProductsModule {
    @Binds
    @PerActivity
    abstract AppCompatActivity providesProductsActivity(ProductsActivity productsActivity);

    @PerFragment
    @ContributesAndroidInjector(modules = AddCategoryFragmentModule.class)
    abstract AddCategoryFragment provideAddCategoryFragmentInjector();

    @PerFragment
    @ContributesAndroidInjector(modules = AddSubCategoryFragmentModule.class)
    abstract AddSubCategoryFragment provideAddSubCategoryFragmentInjector();

    @PerFragment
    @ContributesAndroidInjector(modules = AddProductFragmentModule.class)
    abstract AddProductFragment provideAddProductFragmentInjector();

    @PerFragment
    @ContributesAndroidInjector(modules = ProductListFragmentModule.class)
    abstract ProductsListFragment provideProductsListFragmentInjector();
}
