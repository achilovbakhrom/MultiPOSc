package com.jim.multipos.ui.billing_vendor.di;

import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.core.BaseActivityModule;
import com.jim.multipos.ui.billing_vendor.BillingOperationsActivity;
import com.jim.multipos.ui.billing_vendor.fragments.BillingOperationFragment;
import com.jim.multipos.ui.billing_vendor.presenter.BillingOperationFragmentModule;
import com.jim.multipos.ui.vendor_item_managment.VendorItemsActivity;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by developer on 29.11.2017.
 */
@Module(includes = BaseActivityModule.class)
public abstract class BillingOperationActivityModule {
    @Binds
    @PerActivity
    abstract AppCompatActivity provideInventoryActivity(BillingOperationsActivity billingOperationsActivity);

    @PerFragment
    @ContributesAndroidInjector(modules = BillingOperationFragmentModule.class)
    abstract BillingOperationFragment provideBillingOperationFragment();
}
