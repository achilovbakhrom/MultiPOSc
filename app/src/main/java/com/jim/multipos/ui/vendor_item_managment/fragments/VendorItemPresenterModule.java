package com.jim.multipos.ui.vendor_item_managment.fragments;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.vendor_item_managment.presenter.VendorItemPresenter;
import com.jim.multipos.ui.vendor_item_managment.presenter.VendorItemPresenterImpl;

import dagger.Binds;
import dagger.Module;

/**
 * Created by developer on 20.11.2017.
 */
@Module
public abstract class VendorItemPresenterModule {
    @Binds
    @PerFragment
    abstract VendorItemPresenter provideVendorItemPresenter(VendorItemPresenterImpl itemPresenter);
}
