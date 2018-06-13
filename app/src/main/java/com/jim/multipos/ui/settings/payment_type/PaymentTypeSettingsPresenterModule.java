package com.jim.multipos.ui.settings.payment_type;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class PaymentTypeSettingsPresenterModule {
    @Binds
    @PerFragment
    abstract PaymentTypeSettingsPresenter providePaymentTypeSettingsPresenter(PaymentTypeSettingsPresenterImpl paymentTypeSettingsPresenter);

}
