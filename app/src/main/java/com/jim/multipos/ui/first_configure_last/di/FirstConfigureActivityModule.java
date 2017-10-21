package com.jim.multipos.ui.first_configure_last.di;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.R;
import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.core.BaseActivityModule;
import com.jim.multipos.ui.first_configure_last.FirstConfigureActivity;
import com.jim.multipos.ui.first_configure_last.FirstConfigureView;
import com.jim.multipos.ui.first_configure_last.FirstConfigurePresenter;
import com.jim.multipos.ui.first_configure_last.FirstConfigurePresenterImpl;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

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
        return context.getResources().getStringArray(R.array.weight_title);
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

    @PerActivity
    @Provides
    @Named(value = "first_configure_items")
    static String[] provideFirstConfigureItems(Context context) {
        return context.getResources().getStringArray(R.array.first_configure_items);
    }

    @PerActivity
    @Provides
    @Named(value = "first_configure_items_description")
    static String[] provideFirstConfigureItemsDescription(Context context) {
        return context.getResources().getStringArray(R.array.first_configure_items_description);
    }

}
