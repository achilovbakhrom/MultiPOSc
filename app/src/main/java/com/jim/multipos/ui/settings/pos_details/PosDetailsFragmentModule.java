package com.jim.multipos.ui.settings.pos_details;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

@Module(includes = PosDetailsPresenterModule.class)
public abstract class PosDetailsFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment provideFragment(PosDetailsFragment fragment);

    @Binds
    @PerFragment
    abstract PosDetailsView providePosDetailsView(PosDetailsFragment fragment);
}
