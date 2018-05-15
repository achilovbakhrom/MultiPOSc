package com.jim.multipos.ui.start_configuration.selection_panel;

import com.jim.multipos.core.Presenter;

public interface SelectionPanelPresenter extends Presenter {
    void checkCompletionMode(int position);
    void putCompletion(String key, boolean status);
}
