package com.jim.multipos.ui.settings.payment_type;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.settings.accounts.AccountSettingsFragment;
import com.jim.multipos.ui.settings.accounts.AccountSettingsPresenterModule;
import com.jim.multipos.ui.settings.accounts.AccountSettingsView;

import dagger.Binds;
import dagger.Module;

@Module(includes = PaymentTypeSettingsPresenterModule.class)
public abstract class PaymentTypeSettingsFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment provideFragment(PaymentTypeSettingsFragment paymentTypeSettingsFragment);

    @Binds
    @PerFragment
    abstract PaymentTypeSettingsView providePaymentTypeSettingsView(PaymentTypeSettingsFragment paymentTypeSettingsFragment);
}
