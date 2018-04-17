package com.jim.multipos.ui.reports.payments;

import com.jim.multipos.core.BaseTableReportPresenter;

public interface PaymentsReportPresenter extends BaseTableReportPresenter {
    void filterConfigsChanged(int[] configs);
    void onActionPressed(Object[][] objects, int row, int column);
}
