package com.jim.multipos.ui.settings.choice_panel;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

@Module(includes = {
        ChoicePanelPresenterModule.class
})
public abstract class ChoicePanelFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment provideFragment(ChoicePanelFragment choicePanelFragment);

    @Binds
    @PerFragment
    abstract ChoicePanelView providChoicePanelFragment(ChoicePanelFragment choicePanelFragment);
}
