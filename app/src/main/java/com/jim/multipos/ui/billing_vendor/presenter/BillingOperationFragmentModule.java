package com.jim.multipos.ui.billing_vendor.presenter;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.billing_vendor.fragments.BillingOperationFragment;
import com.jim.multipos.ui.billing_vendor.fragments.BillingOperationPresenrterModule;
import com.jim.multipos.ui.billing_vendor.fragments.BillingOperationView;

import dagger.Binds;
import dagger.Module;

/**
 * Created by developer on 30.11.2017.
 */
@Module(includes = {
        BillingOperationPresenrterModule.class
})
public abstract class BillingOperationFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment provideFragment(BillingOperationFragment billingOperationFragment);

    @Binds
    @PerFragment
    abstract BillingOperationView provideBillingOperationView(BillingOperationFragment billingOperationFragment);
}
