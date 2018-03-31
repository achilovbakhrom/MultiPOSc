package com.jim.multipos.ui.reports.tills;

import com.jim.multipos.core.BaseTableReportPresenter;

/**
 * Created by Sirojiddin on 27.03.2018.
 */

public interface TillsReportPresenter extends BaseTableReportPresenter {
    void openTillDetailsDialog(Object[][] objects, int row, int column);
}
