package com.jim.multipos.ui.reports.order_history;

import com.jim.multipos.core.BaseTableReportPresenter;
import com.jim.multipos.core.Presenter;

import java.util.Calendar;

public interface OrderHistoryPresenter extends BaseTableReportPresenter {
    void onActionClicked(Object[][] objects, int row, int column);
}
