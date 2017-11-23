package com.jim.multipos.ui.vendor_products_view.di;

import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.core.BaseActivityModule;
import com.jim.multipos.ui.vendor_products_view.VendorProductsView;
import com.jim.multipos.ui.vendor_products_view.VendorProductsViewActivity;
import com.jim.multipos.ui.vendor_products_view.VendorProductsViewPresenter;
import com.jim.multipos.ui.vendor_products_view.VendorProductsViewPresenterImpl;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Portable-Acer on 17.11.2017.
 */

@Module(includes = BaseActivityModule.class)
public abstract class VendorProductsViewActivityModule {
    @Binds
    @PerActivity
    abstract AppCompatActivity provideVendorProductsViewActivity(VendorProductsViewActivity activity);

    @Binds
    @PerActivity
    abstract VendorProductsView provideVendorProductsView(VendorProductsViewActivity activity);

    @Binds
    @PerActivity
    abstract VendorProductsViewPresenter provideVendorProductsViewPresenter(VendorProductsViewPresenterImpl presenter);
}
