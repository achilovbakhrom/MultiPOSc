package com.jim.multipos.ui.settings;

import com.jim.multipos.core.BasePresenterImpl;

import javax.inject.Inject;

public class SettingsPresenterImpl extends BasePresenterImpl<SettingsView> implements SettingsPresenter{

    @Inject
    protected SettingsPresenterImpl(SettingsView settingsView) {
        super(settingsView);
    }

}
