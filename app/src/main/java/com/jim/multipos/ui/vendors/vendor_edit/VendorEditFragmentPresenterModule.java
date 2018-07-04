package com.jim.multipos.ui.vendors.vendor_edit;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class VendorEditFragmentPresenterModule {

    @Binds
    @PerFragment
    abstract VendorEditFragmentPresenter provideVendorEditFragmentPresenter(VendorEditFragmentPresenterImpl presenter);

}
