package com.jim.multipos.ui.mainpospage.view;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.consignment.adapter.VendorItemsListAdapter;
import com.jim.multipos.ui.mainpospage.presenter.BarcodeScannerPresenterModule;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Sirojiddin on 20.01.2018.
 */
@Module(
        includes = BarcodeScannerPresenterModule.class
)
public abstract class BarcodeScannerFragmentModule {

    @Binds
    @PerFragment
    abstract Fragment provideFragment(BarcodeScannerFragment barcodeScannerFragment);

    @Binds
    @PerFragment
    abstract BarcodeScannerView provideBarcodeScannerView(BarcodeScannerFragment barcodeScannerFragment);

    @Provides
    @PerFragment
    static VendorItemsListAdapter provideVendorItemsListAdapter(AppCompatActivity context) {
        return new VendorItemsListAdapter(context);
    }
}
