package com.jim.multipos.ui.settings.common;

import com.jim.multipos.core.BasePresenterImpl;

import javax.inject.Inject;

public class CommonConfigPresenterImpl extends BasePresenterImpl<CommonConfigView> implements CommonConfigPresenter {

    @Inject
    protected CommonConfigPresenterImpl(CommonConfigView commonConfigView) {
        super(commonConfigView);
    }
}
