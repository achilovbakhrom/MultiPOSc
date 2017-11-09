package com.jim.multipos.ui.customer_group_new.di;

import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.core.BaseActivityModule;
import com.jim.multipos.ui.customer_group_new.CustomerGroupActivity;
import com.jim.multipos.ui.customer_group_new.CustomerGroupPresenter;
import com.jim.multipos.ui.customer_group_new.CustomerGroupPresenterImpl;
import com.jim.multipos.ui.customer_group_new.CustomerGroupView;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Portable-Acer on 03.11.2017.
 */

@Module(includes = BaseActivityModule.class)
public abstract class CustomerGroupActivityModule {
    @Binds
    @PerActivity
    abstract AppCompatActivity provideCustomerGroupActivity(CustomerGroupActivity activity);

    @Binds
    @PerActivity
    abstract CustomerGroupView provideCustomerGroupView(CustomerGroupActivity activity);

    @Binds
    @PerActivity
    abstract CustomerGroupPresenter provideCustomerGroupPresenter(CustomerGroupPresenterImpl presenter);
}
