package com.jim.multipos.ui.reports.order_history;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class OrderHistoryPresenterModule {
    @Binds
    @PerFragment
    abstract OrderHistoryPresenter provideOrderHistoryPresenter(OrderHistoryPresenterImpl orderHistoryPresenter);
}
