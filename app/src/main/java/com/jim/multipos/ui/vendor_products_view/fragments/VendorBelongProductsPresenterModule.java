package com.jim.multipos.ui.vendor_products_view.fragments;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.vendor_products_view.presenter.VendorBelongProductsListPresenter;
import com.jim.multipos.ui.vendor_products_view.presenter.VendorBelongProductsListPresenterImpl;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class VendorBelongProductsPresenterModule {
    @Binds
    @PerFragment
    abstract VendorBelongProductsListPresenter provideVendorBelongProductsListPresenter(VendorBelongProductsListPresenterImpl vendorBelongProductsListPresenter);
}
