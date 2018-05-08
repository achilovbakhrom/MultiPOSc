package com.jim.multipos.ui.settings.pos_details;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.settings.print.PrintPresenter;
import com.jim.multipos.ui.settings.print.PrintPresenterImpl;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class PosDetailsPresenterModule {
    @Binds
    @PerFragment
    abstract PosDetailsPresenter providePosDetailsPresenter(PosDetailsPresenterImpl presenter);

}
