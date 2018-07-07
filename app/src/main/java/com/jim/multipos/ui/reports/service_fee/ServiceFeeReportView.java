package com.jim.multipos.ui.reports.service_fee;

import com.github.mjdev.libaums.fs.UsbFile;
import com.jim.multipos.core.BaseTableReportView;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.till.Till;

public interface ServiceFeeReportView extends BaseTableReportView {
    void initTable(Object[][] objects);
    void updateTable(Object[][] objects, int position);
    void setSearchResults(Object[][] objectResults, String searchText, int position);
    void showFilterDialog(int[] filterConfig);
    void exportTableToExcel(String fileName, String path, Object[][] objects, int position, String date, String filter, String searchText);
    void exportTableToPdf(String fileName, String path, Object[][] objects, int position, String date, String filter, String searchText);
    void openExportDialog(int position, int mode);
    void exportExcelToUSB(String filename, UsbFile root, Object[][] objects, int position, String date, String filter, String searchText);
    void exportTableToPdfToUSB(String fileName, UsbFile path, Object[][] objects, int position, String date, String filter, String searchText);
    void onOrderPressed(Order order);
    void onTillNotClosed();
    void onTillPressed(DatabaseManager databaseManager, Till till);
    void clearSearch();
}
