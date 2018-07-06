package com.jim.multipos.ui.vendors.di;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.core.BaseActivityModule;
import com.jim.multipos.ui.vendors.VendorActivityPresenter;
import com.jim.multipos.ui.vendors.VendorsActivity;
import com.jim.multipos.ui.vendors.VendorsActivityPresenterImpl;
import com.jim.multipos.ui.vendors.VendorsActivityView;
import com.jim.multipos.ui.vendors.connection.VendorConnection;
import com.jim.multipos.ui.vendors.vendor_edit.VendorEditFragment;
import com.jim.multipos.ui.vendors.vendor_edit.VendorEditFragmentModule;
import com.jim.multipos.ui.vendors.vendor_list.VendorListFragment;
import com.jim.multipos.ui.vendors.vendor_list.VendorListFragmentModule;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

@Module(includes = BaseActivityModule.class)
public abstract class VendorsActivityModule {

    @Binds
    @PerActivity
    abstract AppCompatActivity provideAppCompatActivity(VendorsActivity activity);

    @Binds
    @PerActivity
    abstract VendorsActivityView provideVendorsActivityView(VendorsActivity activity);

    @Binds
    @PerActivity
    abstract VendorActivityPresenter provideVendorActivityPresenter(VendorsActivityPresenterImpl presenter);

    @PerFragment
    @ContributesAndroidInjector(modules = VendorListFragmentModule.class)
    abstract VendorListFragment provideVendorListFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = VendorEditFragmentModule.class)
    abstract VendorEditFragment provideVendorEditFragment();

    @PerActivity
    @Provides
    static VendorConnection provideVendorConnection(Context context){
        return new VendorConnection(context);
    }
}
