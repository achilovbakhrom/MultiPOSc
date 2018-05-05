package com.jim.multipos.ui.settings.choice_panel;

import com.jim.multipos.core.BasePresenterImpl;

import javax.inject.Inject;

public class ChoicePanelPresenterImpl extends BasePresenterImpl<ChoicePanelView> implements ChoicePanelPresenter {

    @Inject
    protected ChoicePanelPresenterImpl(ChoicePanelView choicePanelView) {
        super(choicePanelView);
    }
}
