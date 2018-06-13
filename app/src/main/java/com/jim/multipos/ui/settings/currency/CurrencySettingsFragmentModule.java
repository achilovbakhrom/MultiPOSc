package com.jim.multipos.ui.settings.currency;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.jim.multipos.R;
import com.jim.multipos.config.scope.PerFragment;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module(includes = CurrencyPresenterModule.class)
public abstract class CurrencySettingsFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment provideFragment(CurrencySettingsFragment currencySettingsFragment);

    @Binds
    @PerFragment
    abstract CurrencyView provideCurrencyView(CurrencySettingsFragment currencySettingsFragment);

    @PerFragment
    @Provides
    @Named(value = "currency_name")
    static String[] provideCurrencyName(Context context) {
        return context.getResources().getStringArray(R.array.currency_title);
    }

    @PerFragment
    @Provides
    @Named(value = "currency_abbr")
    static String[] provideCurrencyAbbr(Context context) {
        return context.getResources().getStringArray(R.array.currency_abbrs);
    }
}
