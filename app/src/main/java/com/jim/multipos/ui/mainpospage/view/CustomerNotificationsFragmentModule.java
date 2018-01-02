package com.jim.multipos.ui.mainpospage.view;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.mainpospage.presenter.CustomerNotificationsPresenterModule;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Portable-Acer on 27.10.2017.
 */
@Module(includes = {CustomerNotificationsPresenterModule.class})
public abstract class CustomerNotificationsFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment provideFragment(CustomerNotificationsFragment fragment);

    @Binds
    @PerFragment
    abstract CustomerNotificationsView provideCustomerNotificationsView(CustomerNotificationsFragment fragment);
}
