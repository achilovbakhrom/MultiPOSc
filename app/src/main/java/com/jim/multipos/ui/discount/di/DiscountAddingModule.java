package com.jim.multipos.ui.discount.di;


import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.core.BaseActivityModule;
import com.jim.multipos.ui.discount.DiscountAddingActivity;
import com.jim.multipos.ui.discount.fragments.DiscountAddingFragment;
import com.jim.multipos.ui.discount.presenters.DiscountAddingFragmentModule;
import com.jim.multipos.ui.discount.presenters.DiscountAddingPresenter;
import com.jim.multipos.ui.product_class_new.ProductsClassActivity;
import com.jim.multipos.ui.product_class_new.fragments.ProductsClassFragment;
import com.jim.multipos.ui.product_class_new.presenters.ProductsClassFragmentModule;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by DEV on 27.07.2017.
 */
@Module(includes = BaseActivityModule.class)
public abstract class DiscountAddingModule {

    @Binds
    @PerActivity
    abstract AppCompatActivity provideProductsClassActivity(DiscountAddingActivity discountAddingActivity);

    @PerFragment
    @ContributesAndroidInjector(modules = DiscountAddingFragmentModule.class)
    abstract DiscountAddingFragment provideDiscountAddingFragment();


}
