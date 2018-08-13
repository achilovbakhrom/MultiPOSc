package com.jim.multipos.ui.admin_main_page.fragments.establishment.di;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.admin_main_page.fragments.establishment.EstablishmentAddFragment;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class EstablishmentModule {

    @Binds
    @PerFragment
    abstract Fragment provideFragment(EstablishmentAddFragment fragment);
}
