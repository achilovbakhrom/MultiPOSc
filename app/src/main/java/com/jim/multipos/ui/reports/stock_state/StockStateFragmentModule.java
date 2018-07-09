package com.jim.multipos.ui.reports.stock_state;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.reports.stock_operations.StockOperationFragment;
import com.jim.multipos.ui.reports.stock_operations.StockOperationPresenterModule;
import com.jim.multipos.ui.reports.stock_operations.StockOperationView;

import dagger.Binds;
import dagger.Module;

@Module(
        includes = StockStatePresenterModule.class
)
public abstract class StockStateFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment provideFragment(StockStateFragment stockStateFragment);

    @Binds
    @PerFragment
    abstract StockStateView provideStockStateView(StockStateFragment stockStateFragment);
}
