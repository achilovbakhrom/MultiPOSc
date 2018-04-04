package com.jim.multipos.ui.reports.order_history;

import com.jim.multipos.core.BaseTableReportView;
import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.order.Order;

public interface OrderHistoryView extends BaseTableReportView {
    void setToTable(Object[][] toTable);
    void setToTableFromSearch(Object[][] toTable,String searchedText);
    void showFilterPanel(int[] config);
    void initTable(Object[][] toTable);
    void openOrderDetialsDialog(Order order);
}
