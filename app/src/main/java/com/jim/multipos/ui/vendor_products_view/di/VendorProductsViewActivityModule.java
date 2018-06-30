package com.jim.multipos.ui.vendor_products_view.di;

import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.core.BaseActivityModule;
import com.jim.multipos.ui.reports.order_history.OrderHistoryFragment;
import com.jim.multipos.ui.reports.order_history.OrderHistoryFragmentModule;
import com.jim.multipos.ui.reports.tills.TillsReportFragment;
import com.jim.multipos.ui.reports.tills.TillsReportFragmentModule;
import com.jim.multipos.ui.vendor_products_view.VendorProductsView;
import com.jim.multipos.ui.vendor_products_view.VendorProductsViewActivity;
import com.jim.multipos.ui.vendor_products_view.VendorProductsViewPresenter;
import com.jim.multipos.ui.vendor_products_view.VendorProductsViewPresenterImpl;
import com.jim.multipos.ui.vendor_products_view.fragments.VendorBelongProductsList;
import com.jim.multipos.ui.vendor_products_view.fragments.VendorDetailsFragment;
import com.jim.multipos.ui.vendor_products_view.presenter.VendorBelongProductListFragmentModule;
import com.jim.multipos.ui.vendor_products_view.presenter.VendorDetialsFragmentModule;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Portable-Acer on 17.11.2017.
 */

@Module(includes = BaseActivityModule.class)
public abstract class VendorProductsViewActivityModule {
    @Binds
    @PerActivity
    abstract AppCompatActivity provideVendorProductsViewActivity(VendorProductsViewActivity activity);

    @Binds
    @PerActivity
    abstract VendorProductsView provideVendorProductsView(VendorProductsViewActivity activity);

    @Binds
    @PerActivity
    abstract VendorProductsViewPresenter provideVendorProductsViewPresenter(VendorProductsViewPresenterImpl presenter);

    @PerFragment
    @ContributesAndroidInjector(modules = VendorBelongProductListFragmentModule.class)
    abstract VendorBelongProductsList provideVendorBelongProductsList();

    @PerFragment
    @ContributesAndroidInjector(modules = VendorDetialsFragmentModule.class)
    abstract VendorDetailsFragment provideVendorDetailsFragment();

}
