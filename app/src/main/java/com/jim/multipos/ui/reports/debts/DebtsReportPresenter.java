package com.jim.multipos.ui.reports.debts;

import com.jim.multipos.core.BaseTableReportPresenter;

public interface DebtsReportPresenter extends BaseTableReportPresenter {
    void filterConfigsHaveChanged(int[] config);
}
