package com.jim.multipos.ui.mainpospage.view;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.mainpospage.presenter.OrderListHistoryPresenterModule;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Portable-Acer on 27.10.2017.
 */

@Module(includes = {OrderListHistoryPresenterModule.class})
public abstract class OrderListHistoryFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment provideFragment(OrderListHistoryFragment fragment);

    @Binds
    @PerFragment
    abstract OrderListHistoryView provideOrderListHistoryView(OrderListHistoryFragment fragment);
}
