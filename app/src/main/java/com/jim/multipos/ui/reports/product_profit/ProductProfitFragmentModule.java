package com.jim.multipos.ui.reports.product_profit;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.reports.order_history.OrderHistoryPresenterModule;

import dagger.Binds;
import dagger.Module;

@Module(
        includes = ProductProfitPresenterModule.class
)
public abstract class ProductProfitFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment provideFragment(ProductProfitFragment productProfitFragment);

    @Binds
    @PerFragment
    abstract ProductProfitView provideProductProfitView(ProductProfitFragment productProfitFragment);
}
