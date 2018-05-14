package com.jim.multipos.ui.reports.inventory;

import com.github.mjdev.libaums.fs.UsbFile;
import com.jim.multipos.core.BaseTableReportView;
import com.jim.multipos.data.db.model.order.Order;

public interface InventoryReportView extends BaseTableReportView {
    void initTable(Object[][] objects);
    void updateTable(Object[][] objects, int position);
    void setSearchResults(Object[][] searchResults, String searchText, int position);
    void showFilterDialog(int[] filterConfig);
    void showTillPickerDialog();
    void exportTableToExcel(String fileName, String path, Object[][] objects, int position, String date, String filter, String searchText);
    void exportTableToPdf(String fileName, String path, Object[][] objects, int position, String date, String filter, String searchText);
    void openExportDialog(int position, int mode);
    void exportExcelToUSB(String filename, UsbFile root, Object[][] objects, int position, String date, String filter, String searchText);
    void exportTableToPdfToUSB(String fileName, UsbFile path, Object[][] objects, int position, String date, String filter, String searchText);
    void setTextToSearch(String searchText);
    void onOrderPressed(Order order);
}
