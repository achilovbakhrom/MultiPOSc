package com.jim.multipos.ui.vendors.vendor_edit;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

@Module(includes = VendorEditFragmentPresenterModule.class)
public abstract class VendorEditFragmentModule {

    @Binds
    @PerFragment
    abstract Fragment provideFragment(VendorEditFragment fragment);

    @Binds
    @PerFragment
    abstract VendorEditFragmentView provideVendorEditFragmentView(VendorEditFragment fragment);

}
