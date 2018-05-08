package com.jim.multipos.ui.settings.currency;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.settings.pos_details.PosDetailsPresenter;
import com.jim.multipos.ui.settings.pos_details.PosDetailsPresenterImpl;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class CurrencyPresenterModule {
    @Binds
    @PerFragment
    abstract CurrencyPresenter provideCurrencyPresenter(CurrencyPresenterImpl presenter);

}
