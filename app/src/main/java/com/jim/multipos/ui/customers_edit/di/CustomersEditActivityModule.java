package com.jim.multipos.ui.customers_edit.di;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.R;
import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.core.BaseActivityModule;
import com.jim.multipos.ui.customers_edit.CustomersEditActivity;
import com.jim.multipos.ui.customers_edit.CustomersEditPresenter;
import com.jim.multipos.ui.customers_edit.CustomersEditPresenterImpl;
import com.jim.multipos.ui.customers_edit.CustomersEditView;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * Created by user on 02.09.17.
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

    @PerActivity
    @Provides
    @Named(value = "enter_full_name")
    static String provideEnterFullName(Context context) {
        return context.getString(R.string.enter_full_name);
    }

    @PerActivity
    @Provides
    @Named(value = "enter_phone")
    static String provideEnterPhone(Context context) {
        return context.getString(R.string.enter_phone);
    }

    @PerActivity
    @Provides
    @Named(value = "enter_address")
    static String provideEnterAddress(Context context) {
        return context.getString(R.string.enter_address);
    }
}
