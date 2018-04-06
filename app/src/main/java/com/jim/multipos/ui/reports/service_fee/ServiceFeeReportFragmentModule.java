package com.jim.multipos.ui.reports.service_fee;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.reports.discount.DiscountReportFragment;
import com.jim.multipos.ui.reports.discount.DiscountReportPresenterModule;
import com.jim.multipos.ui.reports.discount.DiscountReportView;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Sirojiddin on 20.01.2018.
 */
@Module(
        includes = ServiceFeeReportPresenterModule.class
)
public abstract class ServiceFeeReportFragmentModule {

    @Binds
    @PerFragment
    abstract Fragment provideFragment(ServiceFeeReportFragment serviceFeeReportFragment);

    @Binds
    @PerFragment
    abstract ServiceFeeReportView provideServiceFeeReportView(ServiceFeeReportFragment serviceFeeReportFragment);

}
