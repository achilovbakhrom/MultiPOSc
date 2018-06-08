package com.jim.multipos.config.common;

import android.app.Application;

import com.jim.multipos.MultiPosApp;
import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.ui.billing_vendor.BillingOperationsActivity;
import com.jim.multipos.ui.billing_vendor.di.BillingOperationActivityModule;
import com.jim.multipos.ui.cash_management.CashManagementActivity;
import com.jim.multipos.ui.cash_management.di.CashManagementActivityModule;
import com.jim.multipos.ui.consignment.ConsignmentActivity;
import com.jim.multipos.ui.consignment.di.ConsignmentActivityModule;
import com.jim.multipos.ui.consignment_list.ConsignmentListActivity;
import com.jim.multipos.ui.consignment_list.di.ConsignmentListActivityModule;
import com.jim.multipos.ui.customer_debt.CustomerDebtActivity;
import com.jim.multipos.ui.customer_debt.di.CustomerDebtActivityModule;
import com.jim.multipos.ui.customer_group_new.CustomerGroupActivity;
import com.jim.multipos.ui.customer_group_new.di.CustomerGroupActivityModule;
import com.jim.multipos.ui.customers.CustomersActivity;
import com.jim.multipos.ui.customers.di.CustomersActivityModule;
import com.jim.multipos.ui.discount.DiscountAddingActivity;
import com.jim.multipos.ui.discount.di.DiscountAddingModule;
import com.jim.multipos.ui.inventory.InventoryActivity;
import com.jim.multipos.ui.inventory.di.InventoryModule;
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
import com.jim.multipos.ui.product_class_new.ProductsClassActivity;
import com.jim.multipos.ui.product_class_new.di.ProductsClassModule;
import com.jim.multipos.ui.product_last.ProductActivity;
import com.jim.multipos.ui.product_last.di.ProductModule;
import com.jim.multipos.ui.reports.ReportsActivity;
import com.jim.multipos.ui.reports.di.ReportsActivityModule;
import com.jim.multipos.ui.secure.AuthActivity;
import com.jim.multipos.ui.secure.di.AuthActivityModule;
import com.jim.multipos.ui.service_fee_new.ServiceFeeActivity;
import com.jim.multipos.ui.service_fee_new.di.ServiceFeeActivityModule;
import com.jim.multipos.ui.settings.SettingsActivity;
import com.jim.multipos.ui.settings.di.SettingsActivityModule;
import com.jim.multipos.ui.signing.SignActivity;
import com.jim.multipos.ui.signing.di.SignActivityModule;
import com.jim.multipos.ui.start_configuration.StartConfigurationActivity;
import com.jim.multipos.ui.start_configuration.di.StartConfigurationActivityModule;
import com.jim.multipos.ui.vendor.add_edit.VendorAddEditActivity;
import com.jim.multipos.ui.vendor.add_edit.di.VendorAddEditActivityModule;
import com.jim.multipos.ui.vendor_item_managment.VendorItemsActivity;
import com.jim.multipos.ui.vendor_item_managment.di.VendorItemMainModule;
import com.jim.multipos.ui.vendor_products_view.VendorProductsViewActivity;
import com.jim.multipos.ui.vendor_products_view.di.VendorProductsViewActivityModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * Created by bakhrom on 10/3/17.
 */

@Module(includes = AndroidSupportInjectionModule.class)
abstract class  AppModule {
    abstract Application application(MultiPosApp app);

    @PerActivity
    @ContributesAndroidInjector(modules = SignActivityModule.class)
    abstract SignActivity provideSignActivity();

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
    @ContributesAndroidInjector(modules = CustomerGroupActivityModule.class)
    abstract CustomerGroupActivity provideCustomerGroupActivity();

    /*@PerActivity
    @ContributesAndroidInjector(modules = com.jim.multipos.ui.customer_group.di.CustomerGroupActivityModule.class)
    abstract com.jim.multipos.ui.customer_group.CustomerGroupActivity provideCustomerGroupActivity();*/

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
    @ContributesAndroidInjector(modules = ServiceFeeActivityModule.class)
    abstract ServiceFeeActivity provideServiceFeeActivity();

    @PerActivity
    @ContributesAndroidInjector(modules = ProductModule.class)
    abstract ProductActivity provideProductActivity();

    @PerActivity
    @ContributesAndroidInjector(modules = ConsignmentActivityModule.class)
    abstract ConsignmentActivity provideConsignmentActivity();

    @PerActivity
    @ContributesAndroidInjector(modules = ConsignmentListActivityModule.class)
    abstract ConsignmentListActivity provideConsignmentListActivity();

    @PerActivity
    @ContributesAndroidInjector(modules = InventoryModule.class)
    abstract InventoryActivity provideInventoryActivity();

    @PerActivity
    @ContributesAndroidInjector(modules = VendorProductsViewActivityModule.class)
    abstract VendorProductsViewActivity provideVendorProductsViewActivity();
    @PerActivity
    @ContributesAndroidInjector(modules = VendorItemMainModule.class)
    abstract VendorItemsActivity provideVendorItemsActivity();

    @PerActivity
    @ContributesAndroidInjector(modules = BillingOperationActivityModule.class)
    abstract BillingOperationsActivity provideBillingOperationsActivity();

    @PerActivity
    @ContributesAndroidInjector(modules = CustomerDebtActivityModule.class)
    abstract CustomerDebtActivity provideCustomerDebtActivity();

    @PerActivity
    @ContributesAndroidInjector(modules = CashManagementActivityModule.class)
    abstract CashManagementActivity provideCashManagementActivity();

    @PerActivity
    @ContributesAndroidInjector(modules = ReportsActivityModule.class)
    abstract ReportsActivity provodeReportsActivity();

    @PerActivity
    @ContributesAndroidInjector(modules = SettingsActivityModule.class)
    abstract SettingsActivity provodeSettingsActivity();

    @PerActivity
    @ContributesAndroidInjector(modules = AuthActivityModule.class)
    abstract AuthActivity provodeForPinnigActivity();

    @PerActivity
    @ContributesAndroidInjector(modules = StartConfigurationActivityModule.class)
    abstract StartConfigurationActivity provideStartConfigurationActivity();

    @PerActivity
    @ContributesAndroidInjector(modules = CustomersActivityModule.class)
    abstract CustomersActivity provideCustomersActivity();
}
