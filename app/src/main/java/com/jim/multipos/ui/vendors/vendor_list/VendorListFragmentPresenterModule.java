package com.jim.multipos.ui.vendors.vendor_list;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class VendorListFragmentPresenterModule {

    @Binds
    @PerFragment
    abstract VendorListFragmentPresenter provideVendorListFragmentPresenter(VendorListFragmentPresenterImpl presenter);
}
