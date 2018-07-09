package com.jim.multipos.ui.reports.stock_operations;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.reports.product_profit.ProductProfitPresenter;
import com.jim.multipos.ui.reports.product_profit.ProductProfitPresenterImpl;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class StockOperationPresenterModule  {
    @Binds
    @PerFragment
    abstract StockOperationPresenter provideStockOperationPresenter(StockOperationPresenterImpl stockOperationPresenter);

}
