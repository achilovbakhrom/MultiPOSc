package com.jim.multipos.ui.reports.stock_operations;

import com.jim.multipos.core.BaseTableReportPresenter;
import com.jim.multipos.core.Presenter;

public interface StockOperationPresenter extends BaseTableReportPresenter {
    void onAction(Object[][] objects, int row, int column);
    void filterConfigsChanged(int[] configs);

}
