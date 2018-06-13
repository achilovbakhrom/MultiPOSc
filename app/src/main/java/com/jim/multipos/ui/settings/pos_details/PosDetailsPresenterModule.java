package com.jim.multipos.ui.settings.pos_details;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class PosDetailsPresenterModule {
    @Binds
    @PerFragment
    abstract PosDetailsPresenter providePosDetailsPresenter(PosDetailsPresenterImpl presenter);

}
