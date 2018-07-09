package com.jim.multipos.ui.reports.stock_operations;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.reports.product_profit.ProductProfitPresenterModule;

import dagger.Binds;
import dagger.Module;

@Module(
        includes = StockOperationPresenterModule.class
)
public abstract class StockOperationFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment provideFragment(StockOperationFragment stockOperationFragment);

    @Binds
    @PerFragment
    abstract StockOperationView provideStockOperationView(StockOperationFragment stockOperationFragment);
}
