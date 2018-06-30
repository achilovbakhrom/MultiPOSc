package com.jim.multipos.ui.vendor_products_view.presenter;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.vendor_products_view.fragments.VendorDetailsFragment;
import com.jim.multipos.ui.vendor_products_view.fragments.VendorDetialPresenterModule;
import com.jim.multipos.ui.vendor_products_view.fragments.VendorDetialsView;

import dagger.Binds;
import dagger.Module;

@Module(includes = {
        VendorDetialPresenterModule.class
})
public abstract class VendorDetialsFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment provideFragment(VendorDetailsFragment vendorDetailsFragment);

    @Binds
    @PerFragment
    abstract VendorDetialsView provideVendorDetialsView(VendorDetailsFragment vendorDetailsFragment);
}
