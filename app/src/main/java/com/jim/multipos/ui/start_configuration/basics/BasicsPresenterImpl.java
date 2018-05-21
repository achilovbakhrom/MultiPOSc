package com.jim.multipos.ui.start_configuration.basics;

import com.jim.multipos.core.BasePresenterImpl;

import javax.inject.Inject;

public class BasicsPresenterImpl extends BasePresenterImpl<BasicsView> implements BasicsPresenter{

    @Inject
    protected BasicsPresenterImpl(BasicsView basicsView) {
        super(basicsView);
    }
}
