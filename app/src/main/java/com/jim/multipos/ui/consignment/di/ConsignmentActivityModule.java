package com.jim.multipos.ui.consignment.di;

import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.core.BaseActivityModule;
import com.jim.multipos.ui.consignment.ConsignmentActivity;
import com.jim.multipos.ui.consignment.ConsignmentActivityPresenter;
import com.jim.multipos.ui.consignment.ConsignmentActivityPresenterImpl;
import com.jim.multipos.ui.consignment.ConsignmentActivityView;
import com.jim.multipos.ui.consignment.view.IncomeConsignmentFragment;
import com.jim.multipos.ui.consignment.view.IncomeConsignmentFragmentModule;
import com.jim.multipos.ui.consignment.view.ReturnConsignmentFragment;
import com.jim.multipos.ui.consignment.view.ReturnConsignmentFragmentModule;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by bakhrom on 10/6/17.
 */

@Module(includes = BaseActivityModule.class)
public abstract class ConsignmentActivityModule {

    @Binds
    @PerActivity
    abstract AppCompatActivity provideConsignmentActivity(ConsignmentActivity consignmentActivity);

    @Binds
    @PerActivity
    abstract ConsignmentActivityPresenter provideConsignmentActivityPresenter(ConsignmentActivityPresenterImpl consignmentActivityPresenter);

    @Binds
    @PerActivity
    abstract ConsignmentActivityView provideConsignmentActivityView(ConsignmentActivity consignmentActivity);

    @PerFragment
    @ContributesAndroidInjector(modules = IncomeConsignmentFragmentModule.class)
    abstract IncomeConsignmentFragment provideIncomeConsignmentFragmentInjector();

    @PerFragment
    @ContributesAndroidInjector(modules = ReturnConsignmentFragmentModule.class)
    abstract ReturnConsignmentFragment provideReturnConsignmentFragmentInjector();
}
