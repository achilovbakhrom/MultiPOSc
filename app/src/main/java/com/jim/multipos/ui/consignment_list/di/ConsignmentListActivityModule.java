package com.jim.multipos.ui.consignment_list.di;

import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.core.BaseActivityModule;
import com.jim.multipos.ui.consignment_list.ConsignmentListActivity;
import com.jim.multipos.ui.consignment_list.ConsignmentListActivityPresenter;
import com.jim.multipos.ui.consignment_list.ConsignmentListActivityPresenterImpl;
import com.jim.multipos.ui.consignment_list.ConsignmentListActivityView;
import com.jim.multipos.ui.consignment_list.view.ConsignmentListFragment;
import com.jim.multipos.ui.consignment_list.view.ConsignmentListFragmentModule;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Sirojiddin on 30.11.2017.
 */

@Module(includes = BaseActivityModule.class)
public abstract class ConsignmentListActivityModule {

    @Binds
    @PerActivity
    abstract AppCompatActivity provideConsignmentListActivity(ConsignmentListActivity consignmentListActivity);

    @Binds
    @PerActivity
    abstract ConsignmentListActivityPresenter provideConsignmentListActivityPresenter(ConsignmentListActivityPresenterImpl consignmentListActivityPresenter);

    @Binds
    @PerActivity
    abstract ConsignmentListActivityView provideConsignmentListActivityView(ConsignmentListActivity consignmentListActivity);

    @PerFragment
    @ContributesAndroidInjector(modules = ConsignmentListFragmentModule.class)
    abstract ConsignmentListFragment provideConsignmentListFragmentInjector();

}
