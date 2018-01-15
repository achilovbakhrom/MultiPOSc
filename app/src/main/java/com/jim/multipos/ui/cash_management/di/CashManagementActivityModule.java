package com.jim.multipos.ui.cash_management.di;

import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.core.BaseActivityModule;
import com.jim.multipos.ui.cash_management.CashManagementActivity;
import com.jim.multipos.ui.cash_management.CashManagementActivityPresenter;
import com.jim.multipos.ui.cash_management.CashManagementActivityPresenterImpl;
import com.jim.multipos.ui.cash_management.CashManagementActivityView;
import com.jim.multipos.ui.cash_management.view.CashDetailsFragment;
import com.jim.multipos.ui.cash_management.view.CashDetailsFragmentModule;
import com.jim.multipos.ui.cash_management.view.CashLogFragment;
import com.jim.multipos.ui.cash_management.view.CashLogFragmentModule;
import com.jim.multipos.ui.cash_management.view.CashOperationsFragmentModule;
import com.jim.multipos.ui.cash_management.view.CashOperationsFragment;
import com.jim.multipos.ui.cash_management.view.CloseTillFirstStepFragment;
import com.jim.multipos.ui.cash_management.view.CloseTillFirstStepFragmentModule;
import com.jim.multipos.ui.cash_management.view.CloseTillSecondStepFragment;
import com.jim.multipos.ui.cash_management.view.CloseTillSecondStepFragmentModule;
import com.jim.multipos.ui.cash_management.view.CloseTillThirdStepFragment;
import com.jim.multipos.ui.cash_management.view.CloseTillThirdStepFragmentModule;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Sirojiddin on 11.01.2018.
 */
@Module(includes = BaseActivityModule.class)
public abstract class CashManagementActivityModule {

    @Binds
    @PerActivity
    abstract AppCompatActivity provudeCashMangementActivity(CashManagementActivity cashManagementActivity);

    @Binds
    @PerActivity
    abstract CashManagementActivityPresenter provideCashManagementActivityPresenter(CashManagementActivityPresenterImpl cashManagementActivityPresenter);

    @Binds
    @PerActivity
    abstract CashManagementActivityView provideCashManagementActivityView(CashManagementActivity cashManagementActivity);

    @PerFragment
    @ContributesAndroidInjector(modules = CashOperationsFragmentModule.class)
    abstract CashOperationsFragment provideCashOperationsFragmentInjector();

    @PerFragment
    @ContributesAndroidInjector(modules = CashLogFragmentModule.class)
    abstract CashLogFragment provideCashLogFragmentInjector();

    @PerFragment
    @ContributesAndroidInjector(modules = CashDetailsFragmentModule.class)
    abstract CashDetailsFragment provideCashDetailsFragmentInjector();

    @PerFragment
    @ContributesAndroidInjector(modules = CloseTillFirstStepFragmentModule.class)
    abstract CloseTillFirstStepFragment provideCloseTillFirstStepFragmentInjector();

    @PerFragment
    @ContributesAndroidInjector(modules = CloseTillSecondStepFragmentModule.class)
    abstract CloseTillSecondStepFragment provideCloseTillSecondStepFragmentInjector();

    @PerFragment
    @ContributesAndroidInjector(modules = CloseTillThirdStepFragmentModule.class)
    abstract CloseTillThirdStepFragment provideCloseTillThirdStepFragmentInjector();
}
