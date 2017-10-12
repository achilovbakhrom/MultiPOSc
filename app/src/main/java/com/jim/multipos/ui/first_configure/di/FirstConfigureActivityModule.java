package com.jim.multipos.ui.first_configure.di;

import android.support.v7.app.AppCompatActivity;
import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.core.BaseActivityModule;
import com.jim.multipos.ui.first_configure.FirstConfigureActivity;
import com.jim.multipos.ui.first_configure.FirstConfigurePresenter;
import com.jim.multipos.ui.first_configure.FirstConfigurePresenterImpl;
import com.jim.multipos.ui.first_configure.FirstConfigureView;
import com.jim.multipos.ui.first_configure.fragments.AccountFragment;
import com.jim.multipos.ui.first_configure.fragments.CurrencyFragment;
import com.jim.multipos.ui.first_configure.fragments.LeftSideFragment;
import com.jim.multipos.ui.first_configure.fragments.PaymentTypeFragment;
import com.jim.multipos.ui.first_configure.fragments.PosDetailsFragment;
import com.jim.multipos.ui.first_configure.fragments.UnitsFragment;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by user on 07.10.17.
 */

@Module(includes = BaseActivityModule.class)
public abstract class FirstConfigureActivityModule {
    @Binds
    @PerActivity
    abstract AppCompatActivity provideFirstConfigureActivity(FirstConfigureActivity firstConfigureActivity);

    @Binds
    @PerActivity
    abstract FirstConfigureView provideFirstConfigureView(FirstConfigureActivity activity);

    @Binds
    @PerActivity
    abstract FirstConfigurePresenter provideFirstConfigurePresenter(FirstConfigurePresenterImpl presenter);

    @PerFragment
    @ContributesAndroidInjector
    abstract PosDetailsFragment providePosDetailsFragment();

    @PerFragment
    @ContributesAndroidInjector
    abstract AccountFragment provideAccountFragment();

    @PerFragment
    @ContributesAndroidInjector
    abstract CurrencyFragment provideCurrencyFragment();

    @PerFragment
    @ContributesAndroidInjector
    abstract PaymentTypeFragment providePaymentTypeFragment();

    @PerFragment
    @ContributesAndroidInjector
    abstract UnitsFragment provideUnitsFragment();

    @PerFragment
    @ContributesAndroidInjector
    abstract LeftSideFragment provideLeftSideFragment();
}
