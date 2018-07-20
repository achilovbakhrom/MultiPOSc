package com.jim.multipos.ui.mainpospage.view;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.consignment.adapter.VendorItemsListAdapter;
import com.jim.multipos.ui.mainpospage.presenter.OrderListPresenterModule;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Portable-Acer on 27.10.2017.
 */

@Module(includes = {OrderListPresenterModule.class})
public abstract class OrderListFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment provideFragment(OrderListFragment fragment);

    @Binds
    @PerFragment
    abstract OrderListView provideOrderListView(OrderListFragment fragment);

    @Provides
    @PerFragment
    static VendorItemsListAdapter provideVendorItemsListAdapter(AppCompatActivity context) {
        return new VendorItemsListAdapter(context);
    }
}
