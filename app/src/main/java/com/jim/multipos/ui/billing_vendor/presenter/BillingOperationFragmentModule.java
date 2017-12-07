package com.jim.multipos.ui.billing_vendor.presenter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.ui.billing_vendor.adapter.BillingOperartionsAdapter;
import com.jim.multipos.ui.billing_vendor.fragments.BillingOperationFragment;
import com.jim.multipos.ui.billing_vendor.fragments.BillingOperationPresenrterModule;
import com.jim.multipos.ui.billing_vendor.fragments.BillingOperationView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * Created by developer on 30.11.2017.
 */
@Module(includes = {
        BillingOperationPresenrterModule.class
})
public abstract class BillingOperationFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment provideFragment(BillingOperationFragment billingOperationFragment);

    @Binds
    @PerFragment
    abstract BillingOperationView provideBillingOperationView(BillingOperationFragment billingOperationFragment);

    @Provides
    @PerFragment
    static BillingOperartionsAdapter provideBillingOperartionsAdapter(AppCompatActivity context, DecimalFormat decimalFormat, DatabaseManager databaseManager){
        return new BillingOperartionsAdapter(context,decimalFormat,databaseManager);
    }
}
