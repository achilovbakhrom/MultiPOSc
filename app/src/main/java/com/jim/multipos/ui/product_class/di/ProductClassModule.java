package com.jim.multipos.ui.product_class.di;


import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.core.BaseActivityModule;
import com.jim.multipos.ui.product_class.ProductClassActivity;
import com.jim.multipos.ui.product_class.fragments.AddProductClassFragment;
import com.jim.multipos.ui.product_class.fragments.ProductClassListFragment;
import com.jim.multipos.ui.product_class.presenters.AddProductClassFragmentModule;
import com.jim.multipos.ui.product_class.presenters.ProductClassListFragmentModule;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by DEV on 27.07.2017.
 */
@Module(includes = BaseActivityModule.class)
public abstract class ProductClassModule {

    @Binds
    @PerActivity
    abstract AppCompatActivity provideSignActivity(ProductClassActivity productClassActivity);

    @PerFragment
    @ContributesAndroidInjector(modules = AddProductClassFragmentModule.class)
    abstract AddProductClassFragment provideAddProductClassFragment();
    @PerFragment
    @ContributesAndroidInjector(modules = ProductClassListFragmentModule.class)
    abstract ProductClassListFragment provideProductClassListFragment();

}
