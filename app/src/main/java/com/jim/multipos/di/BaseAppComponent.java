package com.jim.multipos.di;

import com.jim.multipos.data.network.MultiPosApiModule;
import com.jim.multipos.ui.customer_group.di.CustomerGroupActivityComponent;
import com.jim.multipos.ui.customer_group.di.CustomerGroupActivityModule;
import com.jim.multipos.ui.customers_edit.di.CustomersEditActivityComponent;
import com.jim.multipos.ui.customers_edit.di.CustomersEditActivityModule;
import com.jim.multipos.ui.main_menu.customers_menu.di.CustomersMenuComponent;
import com.jim.multipos.ui.main_menu.customers_menu.di.CustomersMenuModule;
import com.jim.multipos.ui.first_configure.di.FirstConfigureActivityComponent;
import com.jim.multipos.ui.first_configure.di.FirstConfigureActivityModule;
import com.jim.multipos.ui.login.di.LoginPageComponent;
import com.jim.multipos.ui.login.di.LoginPageModule;
import com.jim.multipos.ui.main_menu.employer_menu.di.EmployerMenuComponent;
import com.jim.multipos.ui.main_menu.employer_menu.di.EmployerMenuModule;
import com.jim.multipos.ui.main_menu.inventory_menu.di.InventoryMenuComponent;
import com.jim.multipos.ui.main_menu.inventory_menu.di.InventoryMenuModule;
import com.jim.multipos.ui.mainpospage.di.MainPosPageActivityModule;
import com.jim.multipos.ui.mainpospage.di.MainPosPageActivityComponent;
import com.jim.multipos.ui.main_menu.product_menu.di.ProductMenuComponent;
import com.jim.multipos.ui.main_menu.product_menu.di.ProductMenuModule;
import com.jim.multipos.ui.product_class.di.ProductClassComponent;
import com.jim.multipos.ui.product_class.di.ProductClassModule;
import com.jim.multipos.ui.products.di.ProductsComponent;
import com.jim.multipos.ui.products.di.ProductsModule;
import com.jim.multipos.ui.registration.di.LoginActivityComponent;
import com.jim.multipos.ui.registration.di.LoginActivityModule;
import com.jim.multipos.ui.service_fee.di.ServiceFeeActivityComponent;
import com.jim.multipos.ui.service_fee.di.ServiceFeeActivityModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by DEV on 27.07.2017.
 */
@Singleton
@Component(modules = {BaseAppModule.class, MultiPosApiModule.class})
public interface BaseAppComponent {
    FirstConfigureActivityComponent plus(FirstConfigureActivityModule firstConfigureActivityModule);

    ServiceFeeActivityComponent plus(ServiceFeeActivityModule serviceFeeActivityModule);

    CustomersEditActivityComponent plus(CustomersEditActivityModule customersEditActivityModule);

    CustomerGroupActivityComponent plus(CustomerGroupActivityModule customerGroupActivityModule);

    LoginActivityComponent plus(LoginActivityModule loginActivityModule);

    LoginPageComponent plus(LoginPageModule loginPageModule);

    MainPosPageActivityComponent plus(MainPosPageActivityModule mainPosPageFragmentModule);

    ProductMenuComponent plus(ProductMenuModule productMenuModule);

    CustomersMenuComponent plus(CustomersMenuModule customersMenuModule);

    InventoryMenuComponent plus(InventoryMenuModule inventoryMenuModule);

    EmployerMenuComponent plus(EmployerMenuModule employerMenuModule);

    ProductsComponent plus(ProductsModule productsModule);

    ProductClassComponent plus(ProductClassModule productClassModule);

}
