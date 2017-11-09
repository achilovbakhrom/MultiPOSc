package com.jim.multipos.ui.customers_edit_new.di;

import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.core.BaseActivityModule;
import com.jim.multipos.ui.customers_edit_new.CustomersEditActivity;
import com.jim.multipos.ui.customers_edit_new.CustomersEditPresenter;
import com.jim.multipos.ui.customers_edit_new.CustomersEditPresenterImpl;
import com.jim.multipos.ui.customers_edit_new.CustomersEditView;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Portable-Acer on 04.11.2017.
 */

@Module(includes = BaseActivityModule.class)
public abstract class CustomersEditActivityModule {
    @Binds
    @PerActivity
    abstract AppCompatActivity provideCustomersEditActivity(CustomersEditActivity activity);

    @Binds
    @PerActivity
    abstract CustomersEditView provideCustomersEditView(CustomersEditActivity activity);

    @Binds
    @PerActivity
    abstract CustomersEditPresenter provideCustomersEditPresenter(CustomersEditPresenterImpl presenter);
}
