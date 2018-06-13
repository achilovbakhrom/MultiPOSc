package com.jim.multipos.ui.vendor_item_managment.presenter;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.vendor_item_managment.fragments.VendorItemFragment;
import com.jim.multipos.ui.vendor_item_managment.fragments.VendorItemPresenterModule;
import com.jim.multipos.ui.vendor_item_managment.fragments.VendorItemView;

import dagger.Binds;
import dagger.Module;

/**
 * Created by developer on 20.11.2017.
 */
@Module(includes = {
        VendorItemPresenterModule.class
})
public abstract class VendorItemFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment provideFragment(VendorItemFragment vendorItemFragment);

    @Binds
    @PerFragment
    abstract VendorItemView provideProductsClassFragment(VendorItemFragment vendorItemFragment);
}
