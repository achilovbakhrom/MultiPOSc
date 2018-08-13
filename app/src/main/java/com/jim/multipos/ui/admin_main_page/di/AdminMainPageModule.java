package com.jim.multipos.ui.admin_main_page.di;

import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.core.BaseActivityModule;
import com.jim.multipos.ui.admin_main_page.AdminMainPageActivity;
import com.jim.multipos.ui.admin_main_page.fragments.company.CompanyFragment;
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
import com.jim.multipos.ui.admin_main_page.fragments.establishment.di.EstablishmentModule;
import com.jim.multipos.ui.admin_main_page.fragments.establishment.di.EstablishmentPosModule;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module(includes = BaseActivityModule.class)
public abstract class AdminMainPageModule {

    @Binds
    @PerActivity
    abstract AppCompatActivity provideAdminMainPageActivity(AdminMainPageActivity adminAuthActivity);

    @PerFragment
    @ContributesAndroidInjector(modules = CompanyFragmentModule.class)
    abstract CompanyFragment provideCompanyFragment();

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
    @ContributesAndroidInjector(modules = EstablishmentModule.class)
    abstract EstablishmentAddFragment provideEstablishmentAddFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = EstablishmentPosModule.class)
    abstract EstablishmentFragment provideEstablishmentFragment();
}
