package com.jim.multipos.ui.settings.pos_details;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.settings.print.PrintFragment;
import com.jim.multipos.ui.settings.print.PrintPresenterModule;
import com.jim.multipos.ui.settings.print.PrintView;

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
