package com.jim.multipos.ui.reports.service_fee;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Sirojiddin on 20.01.2018.
 */
@Module
public abstract class ServiceFeeReportPresenterModule {

    @Binds
    @PerFragment
    abstract ServiceFeeReportPresenter provideServiceReportPresenter(ServiceFeeFeeReportPresenterImpl serviceFeeReportPresenter);
}
