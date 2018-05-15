package com.jim.multipos.ui.start_configuration.payment_type;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

@Module(includes = PaymentTypePresenterModule.class)
public abstract class PaymentTypeFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment provideFragment(PaymentTypeFragment fragment);

    @Binds
    @PerFragment
    abstract PaymentTypeView providePaymentTypeView(PaymentTypeFragment fragment);
}
