package com.jim.multipos.ui.customer_group.presenters;

import android.content.Context;

import com.jim.multipos.R;
import com.jim.multipos.config.scope.PerFragment;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * Created by user on 18.10.17.
 */

@Module
public abstract class AddCustomerGroupFragmentPresenterModule {
    @Binds
    @PerFragment
    abstract AddCustomerGroupFragmentPresenter provideAddCustomerGroupFragmentPresenter(AddCustomerGroupFragmentPresenterImpl presenter);

    @PerFragment
    @Provides
    @Named(value = "enter_group_name")
    static String provideEnterGroupName(Context context) {
        return context.getString(R.string.enter_group_name);
    }
}
