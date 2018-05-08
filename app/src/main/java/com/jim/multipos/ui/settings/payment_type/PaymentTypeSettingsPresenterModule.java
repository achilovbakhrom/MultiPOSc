package com.jim.multipos.ui.settings.payment_type;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.settings.accounts.AccountSettingsPresenter;
import com.jim.multipos.ui.settings.accounts.AccountSettingsPresenterImpl;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class PaymentTypeSettingsPresenterModule {
    @Binds
    @PerFragment
    abstract PaymentTypeSettingsPresenter providePaymentTypeSettingsPresenter(PaymentTypeSettingsPresenterImpl paymentTypeSettingsPresenter);

}
