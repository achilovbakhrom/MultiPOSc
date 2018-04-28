package com.jim.multipos.ui.reports.vendor;

import com.github.mjdev.libaums.fs.UsbFile;
import com.jim.multipos.core.BaseTableReportView;
import com.jim.multipos.data.db.model.consignment.Consignment;

public interface VendorReportView extends BaseTableReportView {
    void initTable(Object[][] objects);
    void updateTable(Object[][] objects, int position);
    void openConsignmentId(Consignment consignment);
    void showFilterDialog(int[] filterConfig);
    void setSearchResults(Object[][] objectResults, String searchText, int position);
    void exportTableToExcel(String fileName, String path, Object[][] objects, int position, String date, String filter, String searchText);
    void exportTableToPdf(String fileName, String path, Object[][] objects, int position, String date, String filter, String searchText);
    void openExportDialog(int position, int mode);
    void exportExcelToUSB(String filename, UsbFile root, Object[][] objects, int position, String date, String filter, String searchText);
    void exportTableToPdfToUSB(String fileName, UsbFile path, Object[][] objects, int position, String date, String filter, String searchText);
}
