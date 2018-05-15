package com.jim.multipos.ui.start_configuration.pos_data;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.settings.pos_details.PosDetailsFragment;
import com.jim.multipos.ui.settings.pos_details.PosDetailsPresenterModule;
import com.jim.multipos.ui.settings.pos_details.PosDetailsView;

import dagger.Binds;
import dagger.Module;

@Module(includes = PosDataPresenterModule.class)
public abstract class PosDataFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment provideFragment(PosDataFragment fragment);

    @Binds
    @PerFragment
    abstract PosDataView providePosDataView(PosDataFragment fragment);
}
