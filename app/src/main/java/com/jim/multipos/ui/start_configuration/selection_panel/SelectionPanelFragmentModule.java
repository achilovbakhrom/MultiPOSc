package com.jim.multipos.ui.start_configuration.selection_panel;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.settings.choice_panel.ChoicePanelFragment;
import com.jim.multipos.ui.settings.choice_panel.ChoicePanelPresenterModule;
import com.jim.multipos.ui.settings.choice_panel.ChoicePanelView;

import dagger.Binds;
import dagger.Module;

@Module(includes = SelectionPanelPresenterModule.class)
public abstract class SelectionPanelFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment provideFragment(SelectionPanelFragment fragment);

    @Binds
    @PerFragment
    abstract SelectionPanelView providSelectionPanelView(SelectionPanelFragment fragment);
}
