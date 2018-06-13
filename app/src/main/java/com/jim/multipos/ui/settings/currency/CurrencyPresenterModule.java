package com.jim.multipos.ui.settings.currency;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class CurrencyPresenterModule {
    @Binds
    @PerFragment
    abstract CurrencyPresenter provideCurrencyPresenter(CurrencyPresenterImpl presenter);

}
