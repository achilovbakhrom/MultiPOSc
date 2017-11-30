package com.jim.multipos.ui.billing_vendor.fragments;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.billing_vendor.presenter.BillingOperationPresenter;
import com.jim.multipos.ui.billing_vendor.presenter.BillingOperationPresenterImpl;
import com.jim.multipos.ui.vendor_item_managment.presenter.VendorItemPresenter;

import dagger.Binds;
import dagger.Module;

/**
 * Created by developer on 30.11.2017.
 */
@Module
public abstract class BillingOperationPresenrterModule {
    @Binds
    @PerFragment
    abstract BillingOperationPresenter provideBillingOperationPresenter(BillingOperationPresenterImpl billingOperationPresenter);
}
