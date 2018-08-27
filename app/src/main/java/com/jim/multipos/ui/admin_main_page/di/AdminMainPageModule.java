package com.jim.multipos.ui.admin_main_page.di;

import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.core.BaseActivityModule;
import com.jim.multipos.ui.admin_main_page.AdminMainPageActivity;
import com.jim.multipos.ui.admin_main_page.fragments.company.CompanyAddFragment;
import com.jim.multipos.ui.admin_main_page.fragments.company.CompanyInfoFragment;
import com.jim.multipos.ui.admin_main_page.fragments.company.di.CompanyFragmentModule;
import com.jim.multipos.ui.admin_main_page.fragments.company.di.CompanyInfoFragmentModule;
import com.jim.multipos.ui.admin_main_page.fragments.dashboard.DashboardMainFragment;
import com.jim.multipos.ui.admin_main_page.fragments.dashboard.OrdersFragment;
import com.jim.multipos.ui.admin_main_page.fragments.dashboard.PosFragment;
import com.jim.multipos.ui.admin_main_page.fragments.dashboard.di.DashBoardMainModule;
import com.jim.multipos.ui.admin_main_page.fragments.dashboard.di.OrderModule;
import com.jim.multipos.ui.admin_main_page.fragments.dashboard.di.PosModule;
import com.jim.multipos.ui.admin_main_page.fragments.establishment.EstablishmentAddFragment;
import com.jim.multipos.ui.admin_main_page.fragments.establishment.EstablishmentFragment;
import com.jim.multipos.ui.admin_main_page.fragments.establishment.di.EstablishmentAddModule;
import com.jim.multipos.ui.admin_main_page.fragments.establishment.di.EstablishmentModule;
import com.jim.multipos.ui.admin_main_page.fragments.product.ProductAddFragment;
import com.jim.multipos.ui.admin_main_page.fragments.product.ProductsFragment;
import com.jim.multipos.ui.admin_main_page.fragments.product.di.ProductAddModule;
import com.jim.multipos.ui.admin_main_page.fragments.product.di.ProductListModule;
import com.jim.multipos.ui.admin_main_page.fragments.product_class.ProductClassAddFragment;
import com.jim.multipos.ui.admin_main_page.fragments.product_class.ProductClassFragment;
import com.jim.multipos.ui.admin_main_page.fragments.product_class.di.ProductClassAddModule;
import com.jim.multipos.ui.admin_main_page.fragments.product_class.di.ProductClassModule;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module(includes = BaseActivityModule.class)
public abstract class AdminMainPageModule {

    @Binds
    @PerActivity
    abstract AppCompatActivity provideAdminMainPageActivity(AdminMainPageActivity adminMainPageActivity);
//
//    @Binds
//    @PerActivity
//    abstract AdminMainPageView provideAdminMainPageView(AdminMainPageActivity adminMainPageActivity);
//
//    @Binds
//    @PerActivity
//    abstract AdminMainPagePresenter provideAdminMainPagePresenter(AdminMainPagePresenterImpl presenter);

    @PerFragment
    @ContributesAndroidInjector(modules = CompanyFragmentModule.class)
    abstract CompanyAddFragment provideCompanyFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = CompanyInfoFragmentModule.class)
    abstract CompanyInfoFragment provideCompanyInfoFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = DashBoardMainModule.class)
    abstract DashboardMainFragment provideDashboardMainFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = OrderModule.class)
    abstract OrdersFragment provideOrdersFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = PosModule.class)
    abstract PosFragment providePosFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = EstablishmentAddModule.class)
    abstract EstablishmentAddFragment provideEstablishmentAddFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = EstablishmentModule.class)
    abstract EstablishmentFragment provideEstablishmentFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = ProductAddModule.class)
    abstract ProductAddFragment provideProductAddFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = ProductListModule.class)
    abstract ProductsFragment provideProductsFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = ProductClassAddModule.class)
    abstract ProductClassAddFragment provideProductClassAddFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = ProductClassModule.class)
    abstract ProductClassFragment provideProductClassFragment();
}
