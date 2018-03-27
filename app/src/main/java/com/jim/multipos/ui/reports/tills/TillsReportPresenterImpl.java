package com.jim.multipos.ui.reports.tills;

import com.jim.multipos.core.BasePresenterImpl;

import javax.inject.Inject;

/**
 * Created by Sirojiddin on 27.03.2018.
 */

public class TillsReportPresenterImpl extends BasePresenterImpl<TillsReportView> implements TillsReportPresenter{

    @Inject
    protected TillsReportPresenterImpl(TillsReportView view) {
        super(view);
    }
}
