package com.jim.multipos.ui.reports;

import com.jim.multipos.core.BasePresenterImpl;

import javax.inject.Inject;

/**
 * Created by Sirojiddin on 12.03.2018.
 */

public class ReportsActivityPresenterImpl extends BasePresenterImpl<ReportsActivityView> implements ReportsActivityPresenter{

    @Inject
    protected ReportsActivityPresenterImpl(ReportsActivityView view) {
        super(view);
    }
}
