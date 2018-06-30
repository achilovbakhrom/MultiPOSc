package com.jim.multipos.ui.vendor_products_view.fragments;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.vendor_products_view.presenter.VendorDetialsPresenter;
import com.jim.multipos.ui.vendor_products_view.presenter.VendorDetialsPresenterImpl;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class VendorDetialPresenterModule {
    @Binds
    @PerFragment
    abstract VendorDetialsPresenter provideVendorDetialsPresenter(VendorDetialsPresenterImpl vendorDetialsPresenter);
}
