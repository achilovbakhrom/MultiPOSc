package com.jim.multipos.ui.reports.order_history;


import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

@Module(
        includes = OrderHistoryPresenterModule.class
)
public abstract class OrderHistoryFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment provideFragment(OrderHistoryFragment orderHistoryFragment);

    @Binds
    @PerFragment
    abstract OrderHistoryView provideOrderHistoryView(OrderHistoryFragment orderHistoryFragment);
}
