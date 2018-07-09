package com.jim.multipos.ui.reports.stock_state;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class StockStatePresenterModule {
    @Binds
    @PerFragment
    abstract StockStatePresenter provideStockStatePresenter(StockStatePresenterImpl stockStatePresenter);
}
