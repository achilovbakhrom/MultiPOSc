package com.jim.multipos.ui.customer_debt.di;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.core.BaseActivityModule;
import com.jim.multipos.ui.customer_debt.CustomerDebtActivity;
import com.jim.multipos.ui.customer_debt.CustomerDebtActivityPresenter;
import com.jim.multipos.ui.customer_debt.CustomerDebtActivityPresenterImpl;
import com.jim.multipos.ui.customer_debt.CustomerDebtActivityView;
import com.jim.multipos.ui.customer_debt.connection.CustomerDebtConnection;
import com.jim.multipos.ui.customer_debt.view.CustomerDebtListFragment;
import com.jim.multipos.ui.customer_debt.view.CustomerDebtListFragmentModule;
import com.jim.multipos.ui.customer_debt.view.CustomerListFragment;
import com.jim.multipos.ui.customer_debt.view.CustomerListFragmentModule;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Sirojiddin on 30.11.2017.
 */

@Module(includes = BaseActivityModule.class)
public abstract class CustomerDebtActivityModule {

    @Binds
    @PerActivity
    abstract AppCompatActivity provideCustomerDebtActivity(CustomerDebtActivity customerDebtActivity);

    @Binds
    @PerActivity
    abstract CustomerDebtActivityPresenter provideCustomerDebtActivityPresenter(CustomerDebtActivityPresenterImpl customerDebtPresenter);

    @Binds
    @PerActivity
    abstract CustomerDebtActivityView provideCustomerDebtActivityView(CustomerDebtActivity customerDebtActivity);

    @PerFragment
    @ContributesAndroidInjector(modules = CustomerListFragmentModule.class)
    abstract CustomerListFragment provideCustomerListFragmentInjector();

    @PerFragment
    @ContributesAndroidInjector(modules = CustomerDebtListFragmentModule.class)
    abstract CustomerDebtListFragment provideCustomerDebtListFragmentInjector();

    @PerActivity
    @Provides
    static CustomerDebtConnection provideCustomerDebtConnection(Context context){
        return new CustomerDebtConnection(context);
    }

}
