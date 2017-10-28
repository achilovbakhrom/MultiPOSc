package com.jim.multipos.config.common;

import android.app.Application;

import com.jim.multipos.MultiPosApp;
import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.ui.customer_group.CustomerGroupActivity;
import com.jim.multipos.ui.customer_group.di.CustomerGroupActivityModule;
import com.jim.multipos.ui.customers_edit.CustomersEditActivity;
import com.jim.multipos.ui.customers_edit.di.CustomersEditActivityModule;
import com.jim.multipos.ui.discount.DiscountAddingActivity;
import com.jim.multipos.ui.discount.di.DiscountAddingModule;
import com.jim.multipos.ui.first_configure_last.FirstConfigureActivity;

import com.jim.multipos.ui.first_configure_last.di.FirstConfigureActivityModule;
import com.jim.multipos.ui.lock_screen.LockScreenActivity;
import com.jim.multipos.ui.lock_screen.di.LockScreenActivityModule;
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
import com.jim.multipos.ui.service_fee.ServiceFeeActivity;
import com.jim.multipos.ui.service_fee.di.ServiceFeeActivityModule;
import com.jim.multipos.ui.product_class_new.ProductsClassActivity;
import com.jim.multipos.ui.product_class_new.di.ProductsClassModule;
import com.jim.multipos.ui.product_last.ProductActivity;
import com.jim.multipos.ui.product_last.di.ProductModule;
import com.jim.multipos.ui.signing.SignActivity;
import com.jim.multipos.ui.signing.di.SignActivityModule;
import com.jim.multipos.ui.vendor.add_edit.VendorAddEditActivity;
import com.jim.multipos.ui.vendor.add_edit.di.VendorAddEditActivityModule;

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
    @ContributesAndroidInjector(modules = CustomersEditActivityModule.class)
    abstract CustomersEditActivity provideCustomersEditActivity();

    @PerActivity
    @ContributesAndroidInjector(modules = CustomerGroupActivityModule.class)
    abstract CustomerGroupActivity provideCustomerGroupActivity();

    @PerActivity
    @ContributesAndroidInjector(modules = ServiceFeeActivityModule.class)
    abstract ServiceFeeActivity provideServiceFeeActivity();
    @PerActivity
    @ContributesAndroidInjector(modules = LockScreenActivityModule.class)
    abstract LockScreenActivity provideLockScreenActivity();
    @PerActivity
    @ContributesAndroidInjector(modules = VendorAddEditActivityModule.class)
    abstract VendorAddEditActivity provideVendorAddEditActivity();
    @PerActivity
    @ContributesAndroidInjector(modules = ProductsClassModule.class)
    abstract ProductsClassActivity provideProductsClassActivity();

    @PerActivity
    @ContributesAndroidInjector(modules = DiscountAddingModule.class)
    abstract DiscountAddingActivity provideDiscountAddingActivity();

    @PerActivity
    @ContributesAndroidInjector(modules = ProductModule.class)
    abstract ProductActivity provideProductActivity();
}
