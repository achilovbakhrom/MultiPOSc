package com.jim.multipos.ui.reports.stock_state;

import com.github.mjdev.libaums.fs.UsbFile;
import com.jim.multipos.core.BaseTableReportView;

public interface StockStateView extends BaseTableReportView {
    void initTable(Object[][] objects);
    void updateTable(Object[][] objects, int position);
    void setSearchResults(Object[][] searchResults, String searchText, int position);
    void showFilterDialog(int[] filterConfig);
    void showSummaryFilterDialog(int filterValue);
    void openExportDialog(int currentPosition, int type);
    void exportTableToExcel(String fileName, String path, Object[][] objects, int position, String date, String filter, String searchText);
    void exportTableToExcelUSB(String fileName, UsbFile path, Object[][] objects, int position, String date, String filter, String searchText);
    void exportTableToPdf(String fileName, String path, Object[][] objects, int position, String date, String filter, String searchText);
    void exportTableToPdfUSB(String fileName, UsbFile path, Object[][] objects, int currentPosition, String date, String filter, String searchText);
}
