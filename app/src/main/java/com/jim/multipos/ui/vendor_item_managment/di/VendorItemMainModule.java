package com.jim.multipos.ui.vendor_item_managment.di;

import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.core.BaseActivityModule;
import com.jim.multipos.ui.vendor_item_managment.VendorItemsActivity;
import com.jim.multipos.ui.vendor_item_managment.fragments.VendorItemFragment;
import com.jim.multipos.ui.vendor_item_managment.presenter.VendorItemFragmentModule;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by developer on 20.11.2017.
 */
@Module(includes = BaseActivityModule.class)
public abstract class VendorItemMainModule {
    @Binds
    @PerActivity
    abstract AppCompatActivity provideInventoryActivity(VendorItemsActivity vendorItemsActivity);

    @PerFragment
    @ContributesAndroidInjector(modules = VendorItemFragmentModule.class)
    abstract VendorItemFragment provideVendorItemFragment();
}
