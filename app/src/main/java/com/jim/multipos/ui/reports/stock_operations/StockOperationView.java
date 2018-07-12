package com.jim.multipos.ui.reports.stock_operations;

import com.github.mjdev.libaums.fs.UsbFile;
import com.jim.multipos.core.BaseTableReportView;
import com.jim.multipos.core.BaseView;

public interface StockOperationView extends BaseTableReportView {
    void initTable(Object[][] objects);
    void updateTable(Object[][] objects, int position);
    void setSearchResults(Object[][] searchResults, String searchText, int position);
    void showFilterDialog(int[] filterConfig,int currentPage);
    void openExportDialog(int position, int mode);
    void exportTableToExcel(String fileName, String path, Object[][] objects, int position, String date, String filter, String searchText);
    void exportTableToExcelUSB(String fileName, UsbFile root, Object[][] objects, int position, String date, String filter, String searchText);
    void exportTableToPdfUSB(String fileName, UsbFile root, Object[][] objects, int position, String date, String filter, String searchText);
    void exportTableToPdf(String fileName, String path, Object[][] objects, int position, String date, String filter, String searchText);
}
