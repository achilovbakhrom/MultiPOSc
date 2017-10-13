package com.jim.multipos.ui.first_configure.di;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.MultiPosApp;
import com.jim.multipos.R;
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

import javax.inject.Named;
import javax.inject.Qualifier;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
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

    @PerActivity
    @Provides
    @Named(value = "account_types")
    static String[] provideTypes(Context context) {
        return context.getResources().getStringArray(R.array.first_configure_account_type);
    }

    @PerActivity
    @Provides
    @Named(value = "account_circulations")
    static String[] provideAccountCirculations(Context context) {
        return context.getResources().getStringArray(R.array.first_configure_account_circulation);
    }

    @PerActivity
    @Provides
    @Named(value = "currency_name")
    static String[] provideCurrencyName(Context context) {
        return context.getResources().getStringArray(R.array.base_currencies);
    }

    @PerActivity
    @Provides
    @Named(value = "currency_abbr")
    static String[] provideCurrencyAbbr(Context context) {
        return context.getResources().getStringArray(R.array.base_abbrs);
    }

    @PerActivity
    @Provides
    @Named(value = "base_units_title")
    static String[] provideBaseUnitsTitle(Context context) {
        return context.getResources().getStringArray(R.array.base_units_title);
    }

    @PerActivity
    @Provides
    @Named(value = "base_units_abbr")
    static String[] provideBaseUnitsAbbr(Context context) {
        return context.getResources().getStringArray(R.array.base_units_abbr);
    }

    @PerActivity
    @Provides
    @Named(value = "weight_units_title")
    static String[] provideWeightUnitsTitle(Context context) {
        return context.getResources().getStringArray(R.array.weigth_title);
    }

    @PerActivity
    @Provides
    @Named(value = "length_units_title")
    static String[] provideLengthUnitsTitle(Context context) {
        return context.getResources().getStringArray(R.array.length_title);
    }

    @PerActivity
    @Provides
    @Named(value = "area_units_title")
    static String[] provideAreaUnitsTitle(Context context) {
        return context.getResources().getStringArray(R.array.area_title);
    }

    @PerActivity
    @Provides
    @Named(value = "volume_units_title")
    static String[] provideVolumeUnitsTitle(Context context) {
        return context.getResources().getStringArray(R.array.volume_title);
    }

    @PerActivity
    @Provides
    @Named(value = "weight_units_abbr")
    static String[] provideWeightUnitsAbbr(Context context) {
        return context.getResources().getStringArray(R.array.weigth_abbr);
    }

    @PerActivity
    @Provides
    @Named(value = "length_units_abbr")
    static String[] provideLengthUnitsAbbr(Context context) {
        return context.getResources().getStringArray(R.array.length_abbr);
    }

    @PerActivity
    @Provides
    @Named(value = "area_units_abbr")
    static String[] provideAreaUnitsAbbr(Context context) {
        return context.getResources().getStringArray(R.array.area_abbr);
    }

    @PerActivity
    @Provides
    @Named(value = "volume_units_abbr")
    static String[] provideVolumeUnitsAbbr(Context context) {
        return context.getResources().getStringArray(R.array.volume_abbr);
    }

    @PerActivity
    @Provides
    @Named(value = "weight_units_root_factor")
    static String[] provideWeightUnitsRootFactor(Context context) {
        return context.getResources().getStringArray(R.array.weightRootFactor);
    }

    @PerActivity
    @Provides
    @Named(value = "length_units_root_factor")
    static String[] provideLengthUnitsRootFactor(Context context) {
        return context.getResources().getStringArray(R.array.lengthRootFactor);
    }

    @PerActivity
    @Provides
    @Named(value = "area_units_root_factor")
    static String[] provideAreaUnitsRootFactor(Context context) {
        return context.getResources().getStringArray(R.array.areaRootFactor);
    }

    @PerActivity
    @Provides
    @Named(value = "volume_units_root_factor")
    static String[] provideVolumeUnitsRootFactor(Context context) {
        return context.getResources().getStringArray(R.array.volumeRootFactor);
    }
}
