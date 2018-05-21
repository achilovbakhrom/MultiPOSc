package com.jim.multipos.ui.start_configuration.basics;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.jim.multipos.R;
import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.start_configuration.currency.CurrencyFragment;
import com.jim.multipos.ui.start_configuration.currency.CurrencyPresenterModule;
import com.jim.multipos.ui.start_configuration.currency.CurrencyView;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module(includes = BasicsPresenterModule.class)
public abstract class BasicsFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment provideFragment(BasicsFragment fragment);

    @Binds
    @PerFragment
    abstract BasicsView provideBasicsView(BasicsFragment fragment);
}
