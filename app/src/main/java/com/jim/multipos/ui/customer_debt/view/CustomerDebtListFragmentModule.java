package com.jim.multipos.ui.customer_debt.view;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.customer_debt.adapter.CustomerListAdapter;
import com.jim.multipos.ui.customer_debt.adapter.DebtListAdapter;
import com.jim.multipos.ui.customer_debt.presenter.CustomerDebtListPresenterModule;
import com.jim.multipos.ui.customer_debt.presenter.CustomerListPresenterModule;

import java.text.DecimalFormat;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Sirojiddin on 29.12.2017.
 */
@Module(
        includes = CustomerDebtListPresenterModule.class
)
public abstract class CustomerDebtListFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment provideFragment(CustomerDebtListFragment debtListFragment);

    @Binds
    @PerFragment
    abstract CustomerDebtListView provideCustomerDebtListView(CustomerDebtListFragment debtListFragment);

    @Provides
    @PerFragment
    static DebtListAdapter provideDebtListAdapter(AppCompatActivity context, DecimalFormat decimalFormat) {
        return new DebtListAdapter(context, decimalFormat);
    }
}
