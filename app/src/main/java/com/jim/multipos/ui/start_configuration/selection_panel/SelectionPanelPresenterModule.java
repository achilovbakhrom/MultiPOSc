package com.jim.multipos.ui.start_configuration.selection_panel;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class SelectionPanelPresenterModule {
    @Binds
    @PerFragment
    abstract SelectionPanelPresenter provideSelectionPanelPresenter(SelectionPanelPresenterImpl presenter);
}
