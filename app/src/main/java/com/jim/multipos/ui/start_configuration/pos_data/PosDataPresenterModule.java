package com.jim.multipos.ui.start_configuration.pos_data;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.settings.pos_details.PosDetailsPresenter;
import com.jim.multipos.ui.settings.pos_details.PosDetailsPresenterImpl;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class PosDataPresenterModule {
    @Binds
    @PerFragment
    abstract PosDataPresenter providePosDataPresenter(PosDataPresenterImpl presenter);

}
