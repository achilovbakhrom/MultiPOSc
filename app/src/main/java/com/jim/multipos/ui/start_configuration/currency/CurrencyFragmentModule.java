package com.jim.multipos.ui.start_configuration.currency;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.jim.multipos.R;
import com.jim.multipos.config.scope.PerFragment;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module(includes = CurrencyPresenterModule.class)
public abstract class CurrencyFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment provideFragment(CurrencyFragment fragment);

    @Binds
    @PerFragment
    abstract CurrencyView provideCurrencyView(CurrencyFragment fragment);

    @PerFragment
    @Provides
    @Named(value = "curr_name")
    static String[] provideCurrName(Context context) {
        return context.getResources().getStringArray(R.array.currency_title);
    }

    @PerFragment
    @Provides
    @Named(value = "curr_abbr")
    static String[] provideCurrAbbr(Context context) {
        return context.getResources().getStringArray(R.array.currency_abbrs);
    }
}
