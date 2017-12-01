package com.jim.multipos.ui.consignment_list.view;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.consignment_list.presenter.ConsignmentListPresenterModule;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Sirojiddin on 30.11.2017.
 */

@Module(includes = {
        ConsignmentListPresenterModule.class
})
public abstract class ConsignmentListFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment provideFragment(ConsignmentListFragment consignmentListFragment);

    @Binds
    @PerFragment
    abstract ConsignmentListView provideConsignmentListView(ConsignmentListFragment consignmentListFragment);
}
