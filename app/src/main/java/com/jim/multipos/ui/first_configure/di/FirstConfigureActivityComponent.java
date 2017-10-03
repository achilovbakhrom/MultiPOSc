package com.jim.multipos.ui.first_configure.di;

import com.jim.multipos.ui.first_configure.FirstConfigureActivity;
import com.jim.multipos.ui.ActivityScope;
import com.jim.multipos.ui.first_configure.fragments.AccountFragmentFirstConfig;
import com.jim.multipos.ui.first_configure.fragments.CurrencyFragmentFirstConfig;
import com.jim.multipos.ui.first_configure.fragments.FirstConfigureLeftSideFragment;
import com.jim.multipos.ui.first_configure.fragments.PaymentTypeFragmentFirstConfig;
import com.jim.multipos.ui.first_configure.fragments.PosDetailsFragmentFirstConfig;
import com.jim.multipos.ui.first_configure.fragments.StockFragmentFirstConfig;
import com.jim.multipos.ui.first_configure.fragments.UnitsFragmentFirstConfig;

import dagger.Subcomponent;

/**
 * Created by user on 31.07.17.
 */
@ActivityScope
@Subcomponent(modules = FirstConfigureActivityModule.class)
public interface FirstConfigureActivityComponent {
    void inject(FirstConfigureActivity activity);
    void inject(FirstConfigureLeftSideFragment fragment);
    void inject(PosDetailsFragmentFirstConfig fragment);
    void inject(AccountFragmentFirstConfig fragment);
    void inject(PaymentTypeFragmentFirstConfig fragment);
    void inject(CurrencyFragmentFirstConfig fragment);
    void inject(UnitsFragmentFirstConfig fragment);
    void inject(StockFragmentFirstConfig fragment);
}
