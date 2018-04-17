package com.jim.multipos.ui.reports.payments;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class PaymentsReportPresenterModule {
    @Binds
    @PerFragment
    abstract PaymentsReportPresenter providePaymentsReportPresenter(PaymentsReportPresenterImpl orderHistoryPresenter);
}
