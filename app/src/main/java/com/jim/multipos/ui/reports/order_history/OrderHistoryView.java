package com.jim.multipos.ui.reports.order_history;

import com.github.mjdev.libaums.fs.UsbFile;
import com.jim.multipos.core.BaseTableReportView;
import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.till.Till;

public interface OrderHistoryView extends BaseTableReportView {
    void setToTable(Object[][] toTable);
    void setToTableFromSearch(Object[][] toTable,String searchedText);
    void showFilterPanel(int[] config);
    void initTable(Object[][] toTable);
    void openOrderDetialsDialog(Order order);
    void exportTableToExcel(String fileName, String path, Object[][] objects, String date, String filter, String searchText);
    void exportTableToPdf(String fileName, String path, Object[][] objects, String date, String filter, String searchText);
    void openExportDialog(int mode);
    void exportExcelToUSB(String filename, UsbFile root, Object[][] objects, String date, String filter, String searchText);
    void exportTableToPdfToUSB(String fileName, UsbFile path, Object[][] objects, String date, String filter, String searchText);
    void openTillDetailsDialog(Till till);
    void onTillNotClosed();
}
