package com.jim.multipos.ui.mainpospage.view;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.mainpospage.presenter.PaymentPresenterModule;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Portable-Acer on 27.10.2017.
 */
@Module(includes = {PaymentPresenterModule.class})
public abstract class PaymentFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment provideFragment(PaymentFragment fragment);

    @Binds
    @PerFragment
    abstract PaymentView providePaymentFragment(PaymentFragment fragment);
}
