package com.jim.multipos.ui.start_configuration.payment_type;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class PaymentTypePresenterModule {
    @Binds
    @PerFragment
    abstract PaymentTypePresenter providePaymentTypePresenter(PaymentTypePresenterImpl presenter);

}
