package com.jim.multipos.ui.vendor.add_edit.di;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.R;
import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.core.BaseActivityModule;
import com.jim.multipos.ui.vendor.add_edit.VendorAddEditActivity;
import com.jim.multipos.ui.vendor.add_edit.VendorAddEditPresenter;
import com.jim.multipos.ui.vendor.add_edit.VendorAddEditPresenterImpl;
import com.jim.multipos.ui.vendor.add_edit.VendorAddEditView;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Achilov Bakhrom on 07.10.17.
 */

@Module(includes = BaseActivityModule.class)
public abstract class VendorAddEditActivityModule {

    @Binds
    @PerActivity
    abstract AppCompatActivity provideVendorAddEditActivity(VendorAddEditActivity vendorAddEditActivity);

    @Binds
    @PerActivity
    abstract VendorAddEditView provideVendorAddEditView(VendorAddEditActivity vendorAddEditActivity);

    @Binds
    @PerActivity
    abstract VendorAddEditPresenter provideVendorAddEditPresenter(VendorAddEditPresenterImpl presenter);

    @Provides
    @Named("contact_types")
    static String[] providePhoneTypes(Context context) {
        return context.getResources().getStringArray(R.array.contact_types);
    }
}
