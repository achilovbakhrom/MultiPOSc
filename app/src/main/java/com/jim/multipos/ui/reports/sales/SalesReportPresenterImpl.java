package com.jim.multipos.ui.reports.sales;

import com.jim.multipos.core.BasePresenterImpl;

import javax.inject.Inject;

/**
 * Created by Sirojiddin on 12.03.2018.
 */

public class SalesReportPresenterImpl extends BasePresenterImpl<SalesReportView> implements SalesReportPresenter {

    @Inject
    protected SalesReportPresenterImpl(SalesReportView view) {
        super(view);
    }
}
