package com.jim.multipos.ui.reports.payments;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.reports.order_history.OrderHistoryPresenterModule;

import dagger.Binds;
import dagger.Module;

@Module(
        includes = PaymentsReportPresenterModule.class
)
public abstract class PaymentsReportFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment provideFragment(PaymentsReportFragment paymentsReportFragment);

    @Binds
    @PerFragment
    abstract PaymentsReportView providePaymentsReportView(PaymentsReportFragment paymentsReportFragment);
}
