package com.jim.multipos.ui.settings.choice_panel;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ChoicePanelPresenterModule {
    @Binds
    @PerFragment
    abstract ChoicePanelPresenter provideChoicePanelPresenter(ChoicePanelPresenterImpl choicePanelPresenter);
}
