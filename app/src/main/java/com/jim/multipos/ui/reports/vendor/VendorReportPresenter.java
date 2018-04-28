package com.jim.multipos.ui.reports.vendor;

import com.github.mjdev.libaums.fs.UsbFile;
import com.jim.multipos.core.BaseTableReportPresenter;

public interface VendorReportPresenter extends BaseTableReportPresenter {
    void onConsignmentClicked(Object[][] objects, int row, int column);
    void onFilterChange(int[] config);
    void exportExcel(String fileName, String path);
    void exportPdf(String fileName, String path);
    void exportExcelToUSB(String filename, UsbFile root);
    void exportPdfToUSB(String filename, UsbFile root);
}
