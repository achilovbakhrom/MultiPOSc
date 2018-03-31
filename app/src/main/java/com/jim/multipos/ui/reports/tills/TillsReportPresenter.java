package com.jim.multipos.ui.reports.tills;

import com.jim.multipos.core.Presenter;

/**
 * Created by Sirojiddin on 27.03.2018.
 */

public interface TillsReportPresenter extends Presenter {
    void initTillReportData();
    void openTillDetailsDialog(int row, int column);
}
