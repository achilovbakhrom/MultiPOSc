package com.jim.multipos.config.common;

import android.app.Application;

import com.jim.multipos.MultiPosApp;
import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.ui.discount.DiscountAddingActivity;
import com.jim.multipos.ui.discount.di.DiscountAddingModule;
import com.jim.multipos.ui.first_configure_last.FirstConfigureActivity;

import com.jim.multipos.ui.first_configure_last.di.FirstConfigureActivityModule;
import com.jim.multipos.ui.main_menu.customers_menu.CustomersMenuActivity;
import com.jim.multipos.ui.main_menu.customers_menu.di.CostomersMenuModule;
import com.jim.multipos.ui.main_menu.inventory_menu.InventoryMenuActivity;
import com.jim.multipos.ui.main_menu.inventory_menu.di.InventoryMenuModule;
import com.jim.multipos.ui.main_menu.product_menu.ProductMenuActivity;
import com.jim.multipos.ui.main_menu.product_menu.di.ProductMenuModule;
import com.jim.multipos.ui.mainpospage.MainPosPageActivity;
import com.jim.multipos.ui.mainpospage.di.MainPageMenuModule;
import com.jim.multipos.ui.product.ProductsActivity;
import com.jim.multipos.ui.product.di.ProductsModule;
import com.jim.multipos.ui.product_class.ProductClassActivity;
import com.jim.multipos.ui.product_class.di.ProductClassModule;
import com.jim.multipos.ui.product_class_new.ProductsClassActivity;
import com.jim.multipos.ui.product_class_new.di.ProductsClassModule;
import com.jim.multipos.ui.signing.SignActivity;
import com.jim.multipos.ui.signing.di.SignActivityModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * Created by bakhrom on 10/3/17.
 */

@Module(includes = AndroidSupportInjectionModule.class)
abstract class AppModule {
    abstract Application application(MultiPosApp app);

    @PerActivity
    @ContributesAndroidInjector(modules = SignActivityModule.class)
    abstract SignActivity provideSignActivity();

    @PerActivity
    @ContributesAndroidInjector(modules = ProductsModule.class)
    abstract ProductsActivity provideProductsActivity();
    @PerActivity
    @ContributesAndroidInjector(modules = ProductClassModule.class)
    abstract ProductClassActivity provideProductClassActivity();

    @PerActivity
    @ContributesAndroidInjector(modules = FirstConfigureActivityModule.class)
    abstract FirstConfigureActivity provideFirstConfigureActivity();

    @PerActivity
    @ContributesAndroidInjector(modules = CostomersMenuModule.class)
    abstract CustomersMenuActivity provideCustomersMenuActivity();

    @PerActivity
    @ContributesAndroidInjector(modules = InventoryMenuModule.class)
    abstract InventoryMenuActivity provideInventoryMenuActivity();

    @PerActivity
    @ContributesAndroidInjector(modules = ProductMenuModule.class)
    abstract ProductMenuActivity provideProductMenuActivity();

    @PerActivity
    @ContributesAndroidInjector(modules = MainPageMenuModule.class)
    abstract MainPosPageActivity provideMainPosPageActivity();

    @PerActivity
    @ContributesAndroidInjector(modules = ProductsClassModule.class)
    abstract ProductsClassActivity provideProductsClassActivity();

    @PerActivity
    @ContributesAndroidInjector(modules = DiscountAddingModule.class)
    abstract DiscountAddingActivity provideDiscountAddingActivity();
}
