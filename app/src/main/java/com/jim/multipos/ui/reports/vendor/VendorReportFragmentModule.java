package com.jim.multipos.ui.reports.vendor;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.reports.tills.TillsReportFragment;
import com.jim.multipos.ui.reports.tills.TillsReportPresenterModule;
import com.jim.multipos.ui.reports.tills.TillsReportView;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Sirojiddin on 20.01.2018.
 */
@Module(
        includes = VendorReportPresenterModule.class
)
public abstract class VendorReportFragmentModule {

    @Binds
    @PerFragment
    abstract Fragment provideFragment(VendorReportFragment vendorReportFragment);

    @Binds
    @PerFragment
    abstract VendorReportView provideVendorReportView(VendorReportFragment vendorReportFragment);

}
