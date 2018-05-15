package com.jim.multipos.ui.start_configuration.currency;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class CurrencyPresenterModule {
    @Binds
    @PerFragment
    abstract CurrencyPresenter provideCurrencyPresenter(CurrencyPresenterImpl presenter);

}
