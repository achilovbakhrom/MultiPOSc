package com.jim.multipos.ui.reports.discount;

import com.github.mjdev.libaums.fs.UsbFile;
import com.jim.multipos.core.BaseTableReportView;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.till.Till;

public interface DiscountReportView extends BaseTableReportView {
    void initValues();
    void initTable(Object[][] objects);
    void updateTable(Object[][] objects, int position);
    void showFilterDialog(int[] filterConfig);
    void setSearchResults(Object[][] objectResults, String searchText, int currentPosition);
    void openExportDialog(int position, int mode);
    void exportTableToExcel(String filename, String path, Object[][] objects, int position, String date, String filter, String searchText);
    void exportTableToPdf(String filename, String path, Object[][] objects, int position, String date, String filter, String searchText);
    void exportExcelToUSB(String filename, UsbFile root, Object[][] objects, int position, String date, String filter, String searchText);
    void exportTableToPdfToUSB(String filename, UsbFile root, Object[][] objects, int position, String date, String filter, String searchText);
    void onTillPressed(DatabaseManager databaseManager, Till till);
    void onTillNotClosed();
    void onOrderPressed(Order order);
}
