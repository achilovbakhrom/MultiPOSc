package com.jim.multipos.ui.reports.payments;

import com.jim.multipos.core.BaseTableReportView;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.till.Till;

public interface PaymentsReportView extends BaseTableReportView {
    void initTable(Object[][] objects, int position);
    void updateTable(Object[][] objects, int position);
    void searchTable(Object[][] objects, int position,String searchtext);
    void showFilterPanel(int[] config);
    void onOrderPressed(Order order);
    void onTillPressed(DatabaseManager databaseManager, Till till);
    void onTillNotClosed();
}
