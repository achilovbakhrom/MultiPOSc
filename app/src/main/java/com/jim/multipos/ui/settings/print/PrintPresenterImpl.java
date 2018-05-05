package com.jim.multipos.ui.settings.print;

import com.jim.multipos.core.BasePresenterImpl;

import javax.inject.Inject;

public class PrintPresenterImpl extends BasePresenterImpl<PrintView> implements PrintPresenter {

    @Inject
    protected PrintPresenterImpl(PrintView printView) {
        super(printView);
    }
}
