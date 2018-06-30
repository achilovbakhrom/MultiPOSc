package com.jim.multipos.ui.vendor_products_view.presenter;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.vendor_products_view.fragments.VendorBelongProductsList;
import com.jim.multipos.ui.vendor_products_view.fragments.VendorBelongProductsListView;
import com.jim.multipos.ui.vendor_products_view.fragments.VendorBelongProductsPresenterModule;

import dagger.Binds;
import dagger.Module;

@Module(includes = {
        VendorBelongProductsPresenterModule.class
})
public abstract class VendorBelongProductListFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment  provideFragment(VendorBelongProductsList vendorBelongProductsList);

    @Binds
    @PerFragment
    abstract VendorBelongProductsListView provideVendorBelongProductsListView(VendorBelongProductsList vendorBelongProductsList);
}
