package com.jim.multipos.ui.customer_debt.view;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.customer_debt.adapter.CustomerListAdapter;
import com.jim.multipos.ui.customer_debt.presenter.CustomerListPresenterModule;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Sirojiddin on 29.12.2017.
 */
@Module(
        includes = CustomerListPresenterModule.class
)
public abstract class CustomerListFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment provideFragment(CustomerListFragment customerListFragment);

    @Binds
    @PerFragment
    abstract CustomerListView provideCustomerListView(CustomerListFragment customerListFragment);

    @Provides
    @PerFragment
    static CustomerListAdapter provideCustomerListAdapter(AppCompatActivity context){
        return new CustomerListAdapter(context);
    }
}
