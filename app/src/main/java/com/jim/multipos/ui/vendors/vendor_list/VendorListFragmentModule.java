package com.jim.multipos.ui.vendors.vendor_list;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

@Module(includes = VendorListFragmentPresenterModule.class)
public abstract class VendorListFragmentModule {

    @Binds
    @PerFragment
    abstract Fragment provideFragment(VendorListFragment vendorListFragment);

    @Binds
    @PerFragment
    abstract VendorListFragmentView provideVendorListFragmentView(VendorListFragment fragment);
}
