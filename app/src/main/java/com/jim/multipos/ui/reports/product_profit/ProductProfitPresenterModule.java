package com.jim.multipos.ui.reports.product_profit;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ProductProfitPresenterModule {
    @Binds
    @PerFragment
    abstract ProductProfitPresenter provideProductProfitPresenter(ProductProfitPresenterImpl productProfitPresenter);
}
