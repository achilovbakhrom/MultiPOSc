package com.jim.multipos.ui.reports.hourly_sales;

import com.github.mjdev.libaums.fs.UsbFile;
import com.jim.multipos.core.BaseTableReportView;

public interface HourlySalesReportView extends BaseTableReportView{
    void initTable(Object[][] objects);
    void updateTable(Object[][] objects);
    void exportTableToExcel(String fileName, String path, Object[][] objects, String date);
    void exportTableToPdf(String fileName, String path, Object[][] objects, String date);
    void openExportDialog(int mode);
    void exportExcelToUSB(String filename, UsbFile root, Object[][] objects, String date);
    void exportTableToPdfToUSB(String fileName, UsbFile path, Object[][] objects, String date);
}
