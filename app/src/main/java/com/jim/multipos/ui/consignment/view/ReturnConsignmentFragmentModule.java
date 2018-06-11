package com.jim.multipos.ui.consignment.view;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.ui.consignment.adapter.IncomeItemsListAdapter;
import com.jim.multipos.ui.consignment.adapter.VendorItemsListAdapter;
import com.jim.multipos.ui.consignment.presenter.IncomeConsignmentPresenterModule;
import com.jim.multipos.ui.consignment.presenter.ReturnConsignmentPresenterModule;

import java.text.DecimalFormat;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Sirojiddin on 27.10.2017.
 */
@Module(includes =  {
        ReturnConsignmentPresenterModule.class
})
public abstract class ReturnConsignmentFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment provideFragment(ReturnConsignmentFragment returnConsignmentFragment);

    @Binds
    @PerFragment
    abstract ReturnConsignmentView provideReturnConsignmentView(ReturnConsignmentFragment returnConsignmentFragment);

    @Provides
    @PerFragment
    static IncomeItemsListAdapter provideIncomeItemsListAdapter(AppCompatActivity context, DecimalFormat decimalFormat, DatabaseManager databaseManager){
        return new IncomeItemsListAdapter(context, decimalFormat, databaseManager.getMainCurrency().getAbbr());
    }

    @Provides
    @PerFragment
    static VendorItemsListAdapter provideVendorItemsListAdapter(AppCompatActivity context){
        return new VendorItemsListAdapter(context);
    }
}
